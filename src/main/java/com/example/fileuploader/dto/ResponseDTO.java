package com.example.fileuploader.dto;

import lombok.*;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    HttpStatus httpStatus;
    Integer fileHash;
}
