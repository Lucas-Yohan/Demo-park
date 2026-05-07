package com.lucasyohan.domain.Demo_Park.web.exception;


import java.util.HashMap;

public class ErrorMessage {

    private String message;
    private String path;
    private String method;
    private int status;
    private HashMap<String, String> errors;

    public ErrorMessage() {
    }

    public ErrorMessage(String message, String path, String method, int status) {
        this.message = message;
        this.path = path;
        this.method = method;
        this.status = status;
    }


}
