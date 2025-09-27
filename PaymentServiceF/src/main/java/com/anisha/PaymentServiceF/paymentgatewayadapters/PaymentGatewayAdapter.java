package com.anisha.PaymentServiceF.paymentgatewayadapters;


import com.anisha.PaymentServiceF.dtos.OrderDto;
import com.anisha.PaymentServiceF.models.PaymentGateway;

import java.util.List;

public interface PaymentGatewayAdapter {
    PaymentGateway getGatewayType();
    String createPaymentLink(OrderDto order) throws Exception;
}