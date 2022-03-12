package com.eventproject.utility;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
@Data
public class ApiResponse {
    private HttpStatus status;
    private String message;
    private Map<String, String> errors;

    public ApiResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponse(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiResponse(Map<String, String> errors, String message) {
        this.errors = errors;
        this.message = message;
    }

}
