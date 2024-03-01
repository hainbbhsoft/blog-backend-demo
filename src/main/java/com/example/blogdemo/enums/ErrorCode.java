package com.example.blogdemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("e001"),
    NOT_ACTIVATED_ACCOUNT("e002"),
    WRONG_PASSWORD("e003"),
    DUPLICATE_USERNAME("e004");

    private final String err;
}
