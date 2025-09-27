package com.anisha.OrderServiceF.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public static Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            Object claim = jwt.getClaims().get("userId");
            if (claim instanceof Number number) {
                return number.longValue();
            }
        }
        throw new IllegalStateException("UserId not found in token");
    }
    public static String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            Object claim = jwt.getClaims().get("email");
            if (claim instanceof String email) {
                return email;
            }
        }
        throw new IllegalStateException("UserId not found in token");
    }
}