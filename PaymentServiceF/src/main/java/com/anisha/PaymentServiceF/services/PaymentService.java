package com.anisha.PaymentServiceF.services;

import com.anisha.PaymentServiceF.configs.PaymentsCongif;
import com.anisha.PaymentServiceF.controllers.PaymentController;
import com.anisha.PaymentServiceF.dtos.OrderDto;
import com.anisha.PaymentServiceF.models.Payment;
import com.anisha.PaymentServiceF.models.PaymentStatus;
import com.anisha.PaymentServiceF.paymentgatewayadapters.PaymentGatewayAdapter;
import com.anisha.PaymentServiceF.paymentgatewayadapters.PaymentGatewayStrategy;
import com.anisha.PaymentServiceF.repositories.PaymentRepository;
import com.anisha.PaymentServiceF.repositories.StripeProductOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentGatewayStrategy paymentGatewayStrategy;
    private PaymentsCongif paymentsCongif;
    private PaymentRepository paymentRepository;

    public PaymentService(PaymentGatewayStrategy paymentGatewayStrategy, PaymentsCongif paymentsCongif, PaymentRepository paymentRepository ) {
        this.paymentGatewayStrategy = paymentGatewayStrategy;
        this.paymentsCongif = paymentsCongif;
        this.paymentRepository = paymentRepository;
    }


    public String createPaymentLink(String orderId) {
        // 1. Get the order details from the order service

        OrderDto order = paymentsCongif.callOrderServiceGetOrder("/" + orderId).getBody();

        // 2. Get a payment gateway based upon a `strategy`
        PaymentGatewayAdapter paymentGatewayAdapter = paymentGatewayStrategy.getPaymentGateway();

        String url = "";

        // 3. Call the payment gateway to create a payment link
        try {
            url = paymentGatewayAdapter.createPaymentLink(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if( !url.isBlank()){
            Payment payment = new Payment();
            payment.setEmail(order.getEmail());
            payment.setAmount(order.getTotalAmount());
            payment.setOrderId(orderId);
            payment.setPaymentStatus(PaymentStatus.PENDING);
            payment.setPaymentDate(java.time.LocalDateTime.now());
            payment.setPaymentGateway(paymentGatewayAdapter.getGatewayType());
            paymentRepository.save(payment);
            return url;

        }
        else{
            throw new IllegalArgumentException( "url is empty ");
        }
        //Create a payment object



    }
}