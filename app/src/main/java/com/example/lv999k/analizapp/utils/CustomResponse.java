package com.example.lv999k.analizapp.utils;


import java.util.List;

/**
 * Created by javiercuriel on 11/26/18.
 */


public class CustomResponse<T> {
    int status;
    String name;
    String message;
    String customMessage;
    List<T> results;

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public List<T> getResults() {
        return results;
    }

    public CustomResponse(int status, String name, String message, String customMessage, List<T> results) {
        this.status = status;
        this.name = name;
        this.message = message;
        this.customMessage = customMessage;
        this.results = results;
    }
}
