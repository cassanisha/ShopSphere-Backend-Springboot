package com.anisha.PaymentServiceF.controllers;

import com.anisha.PaymentServiceF.dtos.CreatePaymentLinkRequestDto;
import com.anisha.PaymentServiceF.dtos.CreatePaymentLinkResponseDto;
import com.anisha.PaymentServiceF.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/")
    public CreatePaymentLinkResponseDto createPaymentLink(@RequestBody CreatePaymentLinkRequestDto request) {
        String url = paymentService.createPaymentLink(request.getOrderId());

        CreatePaymentLinkResponseDto response = new CreatePaymentLinkResponseDto();
        response.setUrl(url);

        return response;
    }

}