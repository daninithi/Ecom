package com.example.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private int statusCode;
    private LocalDateTime timestamp;
    private String path;
    
    public ErrorResponse(String message, int statusCode, String path) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }
}
