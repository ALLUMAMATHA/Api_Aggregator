package com.example.API_Aggregator.exception;

import com.example.API_Aggregator.dto.DashboardResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public DashboardResponse handleException(Exception ex) {

        return new DashboardResponse(
                "FAILED",
                "Something went wrong",
                Map.of("error", ex.getMessage())
        );
    }
}