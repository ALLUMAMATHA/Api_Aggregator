package com.example.API_Aggregator.dto;

import java.time.LocalDateTime;

public class DashboardResponse {

    private String status;
    private String message;
    private Object data;
    private LocalDateTime timestamp;

    public DashboardResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}