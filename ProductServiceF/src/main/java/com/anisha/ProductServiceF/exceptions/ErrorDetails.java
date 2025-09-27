package com.anisha.ProductServiceF.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDetails {

    private String error;
    private String details;
    private LocalDateTime timestamp;

    public ErrorDetails() {
        // TODO Auto-generated constructor stub
    }

    public ErrorDetails(String error, String details, LocalDateTime timestamp) {
        super();
        this.error = error;
        this.details = details;
        this.timestamp = timestamp;
    }


}