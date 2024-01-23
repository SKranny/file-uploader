package com.example.fileuploader.feign;

import com.example.fileuploader.dto.FileStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
@Component
@FeignClient(name = "file-status-processor", url = "${file-status-processor-url}")
public interface FileStatusProcessor {
    @GetMapping
    FileStatusDTO checkFileStatus(@RequestParam byte[] fileBytes);
    @PostMapping
    void postStatus(FileStatusDTO fileStatusDTO);

}
