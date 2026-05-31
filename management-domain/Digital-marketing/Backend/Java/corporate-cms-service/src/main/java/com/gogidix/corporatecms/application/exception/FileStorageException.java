package com.gogidix.corporatecms.application.exception;

/**
 * Exception thrown when file storage operations fail.
 */
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    private String code;

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }

}