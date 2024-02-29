package com.example.blogdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String authenticationToken;
    private String username;

    public String toString() {
        return "Token: " + authenticationToken + "\nUsername: " + username;
    }
}
