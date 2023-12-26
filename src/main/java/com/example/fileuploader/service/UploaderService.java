package com.example.fileuploader.service;

import com.example.fileuploader.dto.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UploaderService {
    ResponseDTO doUpload(MultipartFile file);
}
