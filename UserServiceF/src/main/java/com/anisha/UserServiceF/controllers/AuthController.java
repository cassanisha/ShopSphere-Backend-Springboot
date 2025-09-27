package com.anisha.UserServiceF.controllers;

import com.anisha.UserServiceF.dtos.*;

import com.anisha.UserServiceF.models.SessionStatus;
import com.anisha.UserServiceF.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.anisha.UserServiceF.configs.KafkaProducerClient;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;




    public AuthController(AuthService authService ) {
        this.authService = authService;

    }


    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request) {
        return authService.login(request.getEmail(), request.getPassword());

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request) {
        return authService.logout(request.getToken(), request.getUserId());
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request) {
        //can make UserAlreadyExistsException
        UserDto userDto = authService.signUp(request.getEmail(), request.getPassword());

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        SessionStatus sessionStatus = authService.validate(token);

        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
    }

}