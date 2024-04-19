package com.tuan.springbootfinal.exception;

public class ServiceException extends  RuntimeException{
    public ServiceException(String message) {
        super(message);
    }
}
