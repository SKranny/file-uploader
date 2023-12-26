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

        if (!isFileHasBeenUploaded(file.getOriginalFilename())){
            fileStatusProcessor.postStatus(new FileStatusDTO(
                    file.getOriginalFilename(),
                    FileProcessStatus.FILE_ACCEPTED)
            );
        }

        if (!file.isEmpty() && isValidFile(file.getOriginalFilename())){

            if (!isFileHasBeenUploaded(file.getOriginalFilename())){
                kafkaTemplate.send("upload-topic",new FileData(file.getOriginalFilename(), file.getBytes()));
                kafkaTemplate.send("status-topic", new FileStatusDTO(
                        file.getOriginalFilename(),
                        FileProcessStatus.FIRST_VALIDATION_COMPLETED)
                );
            }

            return new ResponseDTO(HttpStatus.OK, file.hashCode());
        }else {
            kafkaTemplate.send("status-topic", new FileStatusDTO(
                    file.getOriginalFilename(),
                    FileProcessStatus.FIRST_VALIDATION_FAILED)
            );
            throw new UnsupportedFileException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "The file extension is not supported"
            );
        }
    }

    private boolean isFileHasBeenUploaded(String fileName){
        FileStatusDTO fileStatusDTO = fileStatusProcessor.checkFileStatus(fileName);
        return fileStatusProcessor.checkFileStatus(fileName)
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
