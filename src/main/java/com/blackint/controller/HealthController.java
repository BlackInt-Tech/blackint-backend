package com.blackint.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "BlackInt Backend Running 🚀";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
