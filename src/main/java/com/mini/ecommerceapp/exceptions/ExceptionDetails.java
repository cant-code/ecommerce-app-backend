package com.mini.ecommerceapp.exceptions;

import java.time.LocalDateTime;

public class ExceptionDetails {
    private LocalDateTime timeStamp;
    private int status;
    private String title;
    private String detail;
    private String developerMessage;

    public ExceptionDetails() {}

    private ExceptionDetails(String title, int status, String detail, LocalDateTime timeStamp, String developerMessage) {
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.timeStamp = timeStamp;
        this.developerMessage = developerMessage;
    }

    public ExceptionDetails setTitle(String title) {
        this.title = title;
        return this;
    }

    public ExceptionDetails setStatus(int status) {
        this.status = status;
        return this;
    }

    public ExceptionDetails setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public ExceptionDetails setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public ExceptionDetails setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
        return this;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }
}
