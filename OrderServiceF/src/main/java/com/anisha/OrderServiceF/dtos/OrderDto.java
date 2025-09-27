package com.anisha.OrderServiceF.dtos;

import com.anisha.OrderServiceF.models.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Double totalAmount;
    private OrderStatus status;
    private Long userId;
    private String email;
    private String orderId;
    private List<Long> productIds;

}