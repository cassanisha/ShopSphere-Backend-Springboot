package com.anisha.PaymentServiceF.paymentgatewayadapters.razorpay;

import com.anisha.PaymentServiceF.dtos.OrderDto;
import com.anisha.PaymentServiceF.models.PaymentGateway;
import com.anisha.PaymentServiceF.paymentgatewayadapters.PaymentGatewayAdapter;

public class RazorpayPaymentGatewayAdapter implements PaymentGatewayAdapter {


    @Override
    public PaymentGateway getGatewayType() {
        return PaymentGateway.RAZORPAY;
    }

    @Override
    public String createPaymentLink(OrderDto order) throws Exception {
        return "";
    }
}