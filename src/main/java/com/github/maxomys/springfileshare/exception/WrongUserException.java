package com.github.maxomys.springfileshare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class WrongUserException extends RuntimeException {

    public WrongUserException() {
        super();
    }

    public WrongUserException(String message) {
        super(message);
    }

}
