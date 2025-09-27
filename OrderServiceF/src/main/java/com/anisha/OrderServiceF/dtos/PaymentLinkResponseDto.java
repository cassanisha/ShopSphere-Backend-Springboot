package com.anisha.OrderServiceF.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentLinkResponseDto {
    private String orderId;
    private String orderStatus;
}