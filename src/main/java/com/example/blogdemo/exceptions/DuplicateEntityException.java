package com.example.blogdemo.exceptions;

import org.springframework.security.core.AuthenticationException;

public class DuplicateEntityException extends AuthenticationException {
    public DuplicateEntityException(String err) {
        super(err);
    }
}
