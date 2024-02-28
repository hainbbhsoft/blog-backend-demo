package com.example.blogdemo.exceptions;

import org.springframework.mail.MailException;

public class SpringRedditException extends Throwable {
    public SpringRedditException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public SpringRedditException(String exMessage) {
        super(exMessage);
    }
}
