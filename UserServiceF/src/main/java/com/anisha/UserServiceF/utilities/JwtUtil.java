package com.anisha.UserServiceF.utilities;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtUtil {

    // Generate a new random 256-bit key
    private static final String SECRET = "kQw1Qn6Qn6v8Qn6Qn6Qw1Qn6Qn6v8Qn6Qn6Qw1Qn6Qn6v8Qn6Qn6Qw1Qn6Qn6v8Qn6";
    public static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    public static void main(String[] args) {
        System.out.println("Your JWT Key (Base64): " + java.util.Base64.getEncoder().encodeToString(KEY.getEncoded()));
    }
}