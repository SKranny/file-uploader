package com.example.fileuploader.controllers;

import com.example.fileuploader.constant.FileProcessStatus;
import com.example.fileuploader.dto.FileStatusDTO;
import com.example.fileuploader.dto.ResponseDTO;
import com.example.fileuploader.feign.FileStatusProcessor;
import com.example.fileuploader.service.UploaderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/upload")
public class UploaderController {
    private final UploaderService uploaderService;

    @PostMapping()
    public ResponseDTO doUpload(@RequestParam("file") MultipartFile file) {
        return uploaderService.doUpload(file);
    }
}
