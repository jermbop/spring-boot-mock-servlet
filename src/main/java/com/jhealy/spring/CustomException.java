package com.jhealy.spring;

public class CustomException extends RuntimeException {

    private int status;

    public CustomException(String message, int status) {
        super(message + status);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
