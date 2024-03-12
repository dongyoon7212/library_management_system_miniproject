package com.study.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/server/health")
    public ResponseEntity<?> check() {
        return ResponseEntity.ok(null);
    }
}
