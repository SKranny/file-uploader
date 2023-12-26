package com.example.fileuploader.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class UnsupportedFileException extends RuntimeException{
    private final HttpStatus httpStatus;

    public UnsupportedFileException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public UnsupportedFileException(String message) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
