package com.eazybytes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticesController {


    @GetMapping("/notices")
    public String getNotices() {
        return "Here are the notices from DB";
    }
}
