package com.example.fileuploader.controllers;

import com.example.fileuploader.dto.ErrorResponseDTO;
import com.example.fileuploader.exception.UnsupportedFileException;
import com.example.fileuploader.model.FileData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(UnsupportedFileException.class)
    public ResponseEntity<ErrorResponseDTO> unsupportedFileExtension(UnsupportedFileException e) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> maxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
