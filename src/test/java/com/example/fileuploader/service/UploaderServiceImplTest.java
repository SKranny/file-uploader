package com.example.fileuploader.service;

import com.example.fileuploader.constant.FileProcessStatus;
import com.example.fileuploader.dto.FileStatusDTO;
import com.example.fileuploader.dto.ResponseDTO;
import com.example.fileuploader.feign.FileStatusProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UploaderServiceImplTest {
    private FileStatusProcessor fileStatusProcessor = mock(FileStatusProcessor.class);
    private UploaderServiceImpl uploaderService = new UploaderServiceImpl(fileStatusProcessor,mock(KafkaTemplate.class));

    @Test
    void doUpload() {
        MultipartFile file = new MockMultipartFile(
                "file",
                "file.xls",
                "type",
                new byte[]{1,2,3}
        );
        ResponseDTO responseDTO = new ResponseDTO(
                HttpStatus.OK,
                file.hashCode()
        );

        try {
            when(fileStatusProcessor.checkFileStatus(file.getBytes())).thenReturn(new FileStatusDTO(
                    "file.xls",
                    FileProcessStatus.FILE_ACCEPTED)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        assertEquals(responseDTO.getHttpStatus(), uploaderService.doUpload(file).getHttpStatus());
    }
}