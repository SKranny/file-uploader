package com.example.fileuploader.model;

import lombok.*;

@Data
@AllArgsConstructor
public class FileData {
    private String fileName;

    private byte[] fileBytes;
}
