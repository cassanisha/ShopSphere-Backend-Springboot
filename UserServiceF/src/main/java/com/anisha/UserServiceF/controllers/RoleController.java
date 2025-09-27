package com.anisha.UserServiceF.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class RoleController {


    @GetMapping("/test")
    public String getHello(){
        return "Hello";
    }

}
