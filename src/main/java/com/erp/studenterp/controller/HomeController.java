package com.erp.studenterp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Student ERP Backend is Running Successfully!";
    }
    @GetMapping("/api/test")
public String test() {
    return "Protected route working!";
}
}