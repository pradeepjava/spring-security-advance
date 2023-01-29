package com.eazybytes.controller;

import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping(value = "hello")
    public String sayHello(){
        return "Say Hello";
    }
}
