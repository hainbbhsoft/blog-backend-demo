package com.example.blogdemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    USER_NOT_FOUND("This username not found: "),
    NOT_ACTIVATED_ACCOUNT("This user is not activated yet: "),
    WRONG_PASSWORD("Wrong password for user: "),
    DUPLICATE_USERNAME("This username has been used: ");

    private final String err;
}
