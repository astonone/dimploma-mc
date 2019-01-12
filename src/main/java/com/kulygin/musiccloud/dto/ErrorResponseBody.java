package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.enumeration.ApplicationErrorTypes;

public class ErrorResponseBody {
    private int code;
    private String message;

    public ErrorResponseBody() {
    }

    public ErrorResponseBody(ApplicationErrorTypes message) {
        this.code = message.getCode();
        this.message = message.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
