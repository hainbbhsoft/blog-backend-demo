package com.example.blogdemo.controller;

import com.example.blogdemo.dto.AuthenticationResponse;
import com.example.blogdemo.dto.ErrorsDto;
import com.example.blogdemo.dto.LoginRequest;
import com.example.blogdemo.dto.RegisterRequest;
import com.example.blogdemo.enums.ErrorCode;
import com.example.blogdemo.enums.ErrorMessage;
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
            return new ResponseEntity<>(new ErrorsDto(ErrorCode.DUPLICATE_USERNAME.getErr(),
                    ErrorMessage.DUPLICATE_USERNAME.getErr() + registerRequest.getUsername()),
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
            return new ResponseEntity<>(new ErrorsDto(ErrorCode.USER_NOT_FOUND.getErr(),
                    ErrorMessage.USER_NOT_FOUND.getErr() + loginRequest.getUsername()),
                    HttpStatus.FORBIDDEN);
        } catch (DisabledException e) {
            return new ResponseEntity<>(new ErrorsDto(ErrorCode.NOT_ACTIVATED_ACCOUNT.getErr(),
                    ErrorMessage.NOT_ACTIVATED_ACCOUNT.getErr() + loginRequest.getUsername()),
                    HttpStatus.FORBIDDEN);
        } catch (CustomAuthenticationException e) {
            return new ResponseEntity<>(new ErrorsDto(ErrorCode.WRONG_PASSWORD.getErr(),
                    ErrorMessage.WRONG_PASSWORD.getErr() + loginRequest.getUsername()),
                    HttpStatus.FORBIDDEN);
        }
    }
}
