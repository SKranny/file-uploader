package com.example.fileuploader.dto;

import lombok.*;
import org.springframework.http.HttpStatus;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
    HttpStatus httpStatus;
    String errorMessage;
}
