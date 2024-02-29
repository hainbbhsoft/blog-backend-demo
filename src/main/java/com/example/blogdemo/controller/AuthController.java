package com.example.blogdemo.controller;

import com.example.blogdemo.dto.AuthenticationResponse;
import com.example.blogdemo.dto.LoginRequest;
import com.example.blogdemo.dto.RegisterRequest;
import com.example.blogdemo.exceptions.SpringRedditException;
import com.example.blogdemo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    //Create controller for auth

    //Call auth service
    private final AuthService authService;

    //Sign up api
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) throws SpringRedditException {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful",
                HttpStatus.OK);
    }

    //Verify endpoint
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) throws SpringRedditException {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successful",
                HttpStatus.OK);
    }

    //Login endpoint
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
