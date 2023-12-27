# File-uploader service

## Description

File-upload-service performs the first stage of validation of the uploaded file, which checks the file type and data size. In case of successful completion of validation, the service notifies another service file-status-processor-service about success and the file is sent to file-processor-service for the second stage of validation.

### Before you start the service you need to make sure that all data servers are up and running.

### The application port is 8082

