package com.example.fileuploader.feign;

import com.example.fileuploader.dto.FileStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
@Component
@FeignClient(name = "file-status-processor", url = "http://localhost:8083/status")
public interface FileStatusProcessor {
    @GetMapping
    FileStatusDTO checkFileStatus(@RequestParam String fileName);
    @PostMapping
    void postStatus(FileStatusDTO fileStatusDTO);

}
