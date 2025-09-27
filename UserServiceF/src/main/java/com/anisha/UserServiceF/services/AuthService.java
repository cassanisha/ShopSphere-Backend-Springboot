package com.anisha.UserServiceF.services;

import com.anisha.UserServiceF.configs.KafkaProducerClient;
import com.anisha.UserServiceF.dtos.SendEmailMessageDto;
import com.anisha.UserServiceF.dtos.UserDto;
import com.anisha.UserServiceF.models.Role;
import com.anisha.UserServiceF.models.Session;
import com.anisha.UserServiceF.models.SessionStatus;
import com.anisha.UserServiceF.models.User;
import com.anisha.UserServiceF.repositories.RoleRepository;
import com.anisha.UserServiceF.repositories.SessionRepository;
import com.anisha.UserServiceF.repositories.UserRepository;
import com.anisha.UserServiceF.utilities.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.PostMapping;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ObjectMapper objectMapper;
    private RoleRepository roleRepository;
    private final KafkaProducerClient kafkaProducerClient;

    @Autowired
    public AuthService(UserRepository userRepository, SessionRepository sessionRepository ,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       ObjectMapper objectMapper, RoleRepository roleRepository,
                       KafkaProducerClient kafkaProducerClient ) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.objectMapper = objectMapper;
        this.roleRepository=roleRepository;
        this.kafkaProducerClient=kafkaProducerClient;
    }

    //login
    public ResponseEntity<UserDto> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            UserDto userDto = this.signUp(email, password);
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        }

        User user = userOptional.get();

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong username password");
////            return null;
        }
        else {
            String token = RandomStringUtils.randomAlphanumeric(30);

            MacAlgorithm alg = Jwts.SIG.HS256; //or HS384 or HS256
            SecretKey key = JwtUtil.KEY;

// Create the compact JWS:

            Map<String, Object> jsonForJwt = new HashMap<>();
            jsonForJwt.put("email", user.getEmail());
            jsonForJwt.put("roles", user.getRoles());
            jsonForJwt.put("createdAt", new Date());
            jsonForJwt.put("expiryAt", Date.from(Instant.now().plusSeconds(3 * 24 * 3600))); // 3 days from now


            token = Jwts.builder()
                    .claims(jsonForJwt)
                    .signWith(key, alg)
                    .compact();
//
//compact// Parse the compact JWS:
//        content = Jwts.parser().verifyWith(key).build().parseSignedContent(jws).getPayload();

            Session session = new Session();
            session.setSessionStatus(SessionStatus.ACTIVE);
            session.setToken(token);
            session.setUser(user);
            session.setExpiringAt((Date) jsonForJwt.get("expiryAt"));
            sessionRepository.save(session);


            UserDto userDto = UserDto.from(user);

//        Map<String, String> headers = new HashMap<>();
//        headers.put(HttpHeaders.SET_COOKIE, token);

            MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
            headers.add(HttpHeaders.SET_COOKIE,  token);


            ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);
//        response.getHeaders().add(HttpHeaders.SET_COOKIE, token);

            return response;
        }
    }

    //logout
    public ResponseEntity<Void> logout(String token, Long userId) {

        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();

        session.setSessionStatus(SessionStatus.ENDED);

        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }

    //signup
    public UserDto signUp(String email, String password) {

        Optional<Role> userRoleOptional = roleRepository.findByRole("ROLE_USER");
        if (userRoleOptional.isEmpty()) {
            throw new RuntimeException("Default role not found");
        }
        Role userRole = userRoleOptional.get();

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRoles(Collections.singleton(userRole));


        User savedUser = userRepository.save(user);

        UserDto userDto = UserDto.from(savedUser);

        try {

            SendEmailMessageDto emailMessage = new SendEmailMessageDto();
            emailMessage.setTo(userDto.getEmail());
            //hardcoded.
            emailMessage.setFrom("anishadhillon08@gmail.com");
            emailMessage.setSubject("Welcome to ShopSphere");
            emailMessage.setBody("Thanks for creating an account. We look forward to you growing. Team ShopSphere");
            System.out.println("Sending message to Kafka");
            kafkaProducerClient.sendMessage("userSignUp", objectMapper.writeValueAsString(emailMessage));
            System.out.println("Message sent to Kafka");

        } catch (Exception e) {
            System.out.println("Not able to send Email Dto message from Auth service");
        }


        return userDto;
    }

    //validate
    public SessionStatus validate(String token) {
        Claims claims = null;
        try {
            // Parse and verify JWT signature
            claims = Jwts.parser()
                    .setSigningKey(JwtUtil.KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("JWT parshhhing failed", e);
        }

        String email = null;
        try {
            email = claims.get("email", String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract email from token", e);
        }

        Date expiryAt = null;
        try {
            expiryAt = claims.get("expiryAt", Date.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract expiry date from token", e);
        }

        Optional<User> userOptional = Optional.empty();
        try {
            userOptional = userRepository.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("User lookup failed", e);
        }

        Optional<Session> sessionOptional = Optional.empty();
        if (userOptional.isPresent()) {
            try {
                Long userId = userOptional.get().getId();
                sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
            } catch (Exception e) {
                throw new RuntimeException("Session lookup failed", e);
            }
        }

        if (sessionOptional.isEmpty()) {
            return SessionStatus.ENDED;
        }

        Session session = sessionOptional.get();

        try {
            if (!session.getSessionStatus().equals(SessionStatus.ACTIVE)) {
                return SessionStatus.ENDED;
            }
        } catch (Exception e) {
            throw new RuntimeException("Session status check failed", e);
        }

        try {
            if (expiryAt.before(new Date())) {
                return SessionStatus.ENDED;
            }
        } catch (Exception e) {
            throw new RuntimeException("Expiry check failed", e);
        }

        return SessionStatus.ACTIVE;
    }

}

/// GET /products/1 -> abcd1234abcd
//        /authentication/validate/abcd1234abcd>u_id=12 -> true
// if the token is any random strong, we will first need to make
// a db call to validate the token
// and then another call to get the details of the user
// auth-token%3AeyJjdHkiOiJ0ZXh0L3BsYWluIiwiYWxnIjoiSFMyNTYifQ.ewogICAiZW1haWwiOiAibmFtYW5Ac2NhbGVyLmNvbSIsCiAgICJyb2xlcyI6IFsKICAgICAgIm1lbnRvciIsCiAgICAgICJ0YSIKICAgXSwKICAgImV4cGlyYXRpb25EYXRlIjogIjIzcmRPY3RvYmVyMjAyMyIKfQ.r2FVQUCn6DNHir5AlEBT2XQMgO7aN4m3xg9zcuB-zxQ
// auth-token%3AeyJhbGciOiJIUzI1NiJ9.eyJjcmVhdGVkQXQiOjE2OTgwNzgzNDg0NTQsInJvbGVzIjpbXSwiZXhwaXJ5QXQiOjE5NjU2LCJlbWFpbCI6Im5hbWFuQHNjYWxlci5jb20ifQ._v1af8cc1YA-cEyHlX1BASwveBiASQeteWFM8UzWxfY

// {
//   to: "",
//   from: "",
//   subject: "",
//   body: ""
// }