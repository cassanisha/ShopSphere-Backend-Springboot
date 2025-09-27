package com.anisha.ProductServiceF.requests;

import lombok.Data;

@Data
public class AddItemRequest {

    private Long productId;
    private String size;
    private int quantity;
    private Integer price;

    public AddItemRequest() {

    }


}

