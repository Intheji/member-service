package com.memberservice.common.exception;

public class FileUploadFailException extends RuntimeException {
    public FileUploadFailException(String message) {
        super(message);
    }
}
