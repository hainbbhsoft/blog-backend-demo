package com.example.blogdemo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
@AllArgsConstructor
public class DemoController {
    @GetMapping("/get-demo")
    public ResponseEntity<String> sayHello() {
        return new ResponseEntity<>("Hello from valid endpoint", HttpStatus.OK);
    }
}
