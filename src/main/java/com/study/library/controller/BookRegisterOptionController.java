package com.study.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/book/option")
public class BookRegisterOptionController {

    @GetMapping("/type")
    public ResponseEntity<?> getBookType() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(null);
    }
}
