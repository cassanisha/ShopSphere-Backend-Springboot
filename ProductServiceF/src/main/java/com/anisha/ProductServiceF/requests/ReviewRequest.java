package com.anisha.ProductServiceF.requests;

import lombok.Data;

@Data
public class ReviewRequest {

    private Long productId;
    private String review;


}