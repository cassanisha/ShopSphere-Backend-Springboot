package com.anisha.OrderServiceF.controllers;

import com.anisha.OrderServiceF.configs.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/userid")
    public String printUserId() {
        Long userId = JwtService.getUserId();
        return "User ID from token: " + userId;
    }
}