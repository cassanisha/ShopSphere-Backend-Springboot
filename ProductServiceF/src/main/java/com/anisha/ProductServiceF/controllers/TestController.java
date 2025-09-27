package com.anisha.ProductServiceF.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Hello from Product Service!";
    }
}
