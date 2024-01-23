package com.example.fileuploader.dto;

import com.example.fileuploader.constant.FileProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileStatusDTO {
    private String fileName;
    private byte[] fileBytes;
    private FileProcessStatus fileStatus;
}
