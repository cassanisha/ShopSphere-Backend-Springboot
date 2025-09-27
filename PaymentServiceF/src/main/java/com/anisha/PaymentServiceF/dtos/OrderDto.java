package com.anisha.PaymentServiceF.dtos;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Double totalAmount;
    private String orderId;
    private List<Long> productIds;
    private String Email;
}