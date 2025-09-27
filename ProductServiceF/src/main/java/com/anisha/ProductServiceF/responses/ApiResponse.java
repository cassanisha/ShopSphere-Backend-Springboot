package com.anisha.ProductServiceF.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ApiResponse {

    private String message;
    private boolean status;

    public ApiResponse(String message, boolean status) {
        super();
        this.message = message;
        this.status = status;
    }

    public ApiResponse() {

    }
}
