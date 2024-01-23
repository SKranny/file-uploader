package com.example.fileuploader.service;

import com.example.fileuploader.constant.FileProcessStatus;
import com.example.fileuploader.dto.FileStatusDTO;
import com.example.fileuploader.dto.ResponseDTO;
import com.example.fileuploader.exception.UnsupportedFileException;
import com.example.fileuploader.feign.FileStatusProcessor;
import com.example.fileuploader.model.FileData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploaderServiceImpl implements UploaderService {

    private final FileStatusProcessor fileStatusProcessor;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @SneakyThrows
    @Override
    public ResponseDTO doUpload(MultipartFile file) throws UnsupportedFileException{

        if (!isFileHasBeenUploaded(file.getBytes())){ // проверить по содержимому файла
            fileStatusProcessor.postStatus(FileStatusDTO.builder()
                    .fileName(file.getOriginalFilename())
                    .fileStatus(FileProcessStatus.FILE_ACCEPTED)
                    .build()
            );
        }

        if (!file.isEmpty() && isValidFile(file.getOriginalFilename())){

            if (!isFileHasBeenUploaded(file.getBytes())){
                kafkaTemplate.send("upload-topic",new FileData(file.getOriginalFilename(), file.getBytes()));
                kafkaTemplate.send("status-topic", FileStatusDTO.builder()
                        .fileStatus(FileProcessStatus.FIRST_VALIDATION_COMPLETED)
                        .fileName(file.getOriginalFilename())
                        .build()
                );
            }

            return new ResponseDTO(HttpStatus.OK, file.hashCode());
        }else {
            kafkaTemplate.send("status-topic", FileStatusDTO.builder()
                    .fileStatus(FileProcessStatus.FIRST_VALIDATION_FAILED)
                    .fileName(file.getOriginalFilename())
                    .build()
            );
            throw new UnsupportedFileException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "The file extension is not supported"
            );
        }
    }

    private boolean isFileHasBeenUploaded(byte[] fileBytes){
        return fileStatusProcessor.checkFileStatus(FileStatusDTO.builder()
                        .fileBytes(fileBytes)
                        .build())
                .getFileStatus().equals(FileProcessStatus.FILE_HAS_BEEN_UPLOADED);
    }

    private boolean isValidFile(String fileName){
        String fileExtension = getFileExtension(fileName);
        return fileExtension.equals(".xls") || fileExtension.equals(".xlsx");
    }

    private String getFileExtension(String fileNameWithExtension){
        int start = fileNameWithExtension.lastIndexOf('.');
        return fileNameWithExtension.substring(start);
    }
}
