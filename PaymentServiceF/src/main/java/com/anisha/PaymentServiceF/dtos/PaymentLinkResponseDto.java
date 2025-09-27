package com.anisha.PaymentServiceF.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentLinkResponseDto {
    private String orderId;
    private String orderStatus;
}