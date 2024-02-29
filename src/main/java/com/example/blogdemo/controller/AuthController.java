package com.example.blogdemo.controller;

import com.example.blogdemo.dto.AuthenticationResponse;
import com.example.blogdemo.dto.ErrorsDto;
import com.example.blogdemo.dto.LoginRequest;
import com.example.blogdemo.dto.RegisterRequest;
import com.example.blogdemo.exceptions.CustomAuthenticationException;
import com.example.blogdemo.exceptions.DuplicateEntityException;
import com.example.blogdemo.exceptions.SpringRedditException;
import com.example.blogdemo.service.AuthService;
import com.example.blogdemo.service.jwt.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    //Create controller for auth

    //Call auth service
    private final AuthService authService;

    private final UserDetailsServiceImpl userDetailsService;

    //Sign up api
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody RegisterRequest registerRequest) throws SpringRedditException {
        try {
            authService.signup(registerRequest);
            return new ResponseEntity<>("User Registration Successful",
                    HttpStatus.OK);
        } catch (DuplicateEntityException e) {
            return new ResponseEntity<>(new ErrorsDto("err004", "Username " + registerRequest.getUsername() + " has been selected. Please choose another one"),
                    HttpStatus.FORBIDDEN);
        }
    }

    //Verify account endpoint
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) throws SpringRedditException {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successful",
                HttpStatus.OK);
    }

    //Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthenticationResponse authenticationResponse = authService.login(loginRequest);
            return new ResponseEntity<>(authenticationResponse,
                    HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(new ErrorsDto("e001", "User not found name: " + loginRequest.getUsername()),
                    HttpStatus.FORBIDDEN);
        } catch (DisabledException e) {
            return new ResponseEntity<>(new ErrorsDto("e002",
                    "User " + loginRequest.getUsername() + " account is not activated. Please verify your account"),
                    HttpStatus.FORBIDDEN);
        } catch (CustomAuthenticationException e) {
            return new ResponseEntity<>(new ErrorsDto("e003",
                    "Wrong password for: " + loginRequest.getUsername()),
                    HttpStatus.FORBIDDEN);
        }
    }
}
