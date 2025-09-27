package com.anisha.PaymentServiceF.controllers;

import com.anisha.PaymentServiceF.configs.KafkaProducerClient;
import com.anisha.PaymentServiceF.configs.PaymentsCongif;
import com.anisha.PaymentServiceF.dtos.PaymentLinkResponseDto;
import com.anisha.PaymentServiceF.dtos.SendEmailMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.anisha.PaymentServiceF.dtos.PaymentPayload;

@RestController
@RequestMapping("/payments")
public class PaymentWebhookController {
    private PaymentsCongif paymentsCongif;
    private KafkaProducerClient kafkaProducerClient;
    private ObjectMapper objectMapper;

    public PaymentWebhookController(
            PaymentsCongif paymentsCongif,
            KafkaProducerClient kafkaProducerClient,
            ObjectMapper objectMapper) {
        this.paymentsCongif = paymentsCongif;
        this.kafkaProducerClient = kafkaProducerClient;
        this.objectMapper = objectMapper;
    }
    /*
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            System.out.println("Webhook received: " + payload);
            System.out.println("SIGhEAD received: " + sigHeader);

            String endpointSecret = "whsec_214308cd4173c9c16ba63d2c856d4881eda71b450d5e19cf2d40a74eff7694da"; //STRIPE CLI

            Event event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow();
            String orderId = paymentIntent.getMetadata().get("orderId");
            String emailId = paymentIntent.getMetadata().get("emailId");
            switch (event.getType()) {
                case "payment_intent.succeeded": {

                    System.out.println(" Payment succeeded: " + paymentIntent.getId());
                    //call order service to set order status//and get access token from authserver
                    paymentsCongif.callOrderServicePlaceOrder(orderId);

                    //send kafka message to broker, to order service , telling orderStatus.
                    PaymentLinkResponseDto orderDto = new PaymentLinkResponseDto();
                    orderDto.setOrderId(orderId);
                    orderDto.setOrderStatus("Success");
                    kafkaProducerClient.sendMessage("orderServiceStatus", objectMapper.writeValueAsString(orderDto));

                    //send kafka message to broker, to EMAILsERVICE
                    SendEmailMessageDto emailMessage = new SendEmailMessageDto();
                    emailMessage.setSuccessfulPaymentMsg(emailId);
                    kafkaProducerClient.sendMessage("orderStatus", objectMapper.writeValueAsString(emailMessage));


                    break;
                }

                case "payment_intent.payment_failed": {
                    PaymentIntent failedIntent = (PaymentIntent) event.getDataObjectDeserializer()
                            .getObject()
                            .orElseThrow();
                    System.out.println("web Payment failed: " + failedIntent.getId());
                    //call order service to set order status//and get access token from authserver
                    paymentsCongif.callOrderServiceFailedOrder(orderId);

                    //send kafka message to broker, to order service , telling orderStatus.
                    PaymentLinkResponseDto orderDto = new PaymentLinkResponseDto();
                    orderDto.setOrderId(orderId);
                    orderDto.setOrderStatus("Failure");
                    kafkaProducerClient.sendMessage("orderServiceStatus", objectMapper.writeValueAsString(orderDto));

                    //send kafka message to broker, to EMAILsERVICE
                    SendEmailMessageDto emailMessage = new SendEmailMessageDto();
                    emailMessage.setFailedPaymentMsg(emailId);
                    kafkaProducerClient.sendMessage("orderStatus", objectMapper.writeValueAsString(emailMessage));
                    break;
                }
                default:
                    System.out.println("Unhandled event type: " + event.getType());
            }
            return ResponseEntity.ok("Webhook received");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: " + e.getMessage());
        }
        */

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody PaymentPayload paymentPayload) {

        try {


            String orderId = paymentPayload.getOrderId();
            String emailId = paymentPayload.getEmailId();
            String event= paymentPayload.getEvent();
            switch ( event ) {
                case "payment_intent.succeeded": {

                    System.out.println(" Payment succeeded: " + orderId);
                    //call order service to set order status//and get access token from authserver
                    //paymentsCongif.callOrderServicePlaceOrder(orderId);

                    //send kafka message to broker, to order service , telling orderStatus.
                    PaymentLinkResponseDto orderDto = new PaymentLinkResponseDto();
                    orderDto.setOrderId(orderId);
                    orderDto.setOrderStatus("Success");
                    kafkaProducerClient.sendMessage("orderServiceStatus", objectMapper.writeValueAsString(orderDto));

                    //send kafka message to broker, to EMAILsERVICE
                    SendEmailMessageDto emailMessage = new SendEmailMessageDto();
                    emailMessage.setSuccessfulPaymentMsg(emailId, orderId);
                    kafkaProducerClient.sendMessage("orderStatus", objectMapper.writeValueAsString(emailMessage));


                    break;
                }

                case "payment_intent.payment_failed": {

                    System.out.println("web Payment failed: " + orderId);
                    //call order service to set order status//and get access token from authserver
                    //paymentsCongif.callOrderServiceFailedOrder(orderId);

                    //send kafka message to broker, to order service , telling orderStatus.
                    PaymentLinkResponseDto orderDto = new PaymentLinkResponseDto();
                    orderDto.setOrderId(orderId);
                    orderDto.setOrderStatus("Failure");
                    kafkaProducerClient.sendMessage("orderServiceStatus", objectMapper.writeValueAsString(orderDto));

                    //send kafka message to broker, to EMAILsERVICE
                    SendEmailMessageDto emailMessage = new SendEmailMessageDto();
                    emailMessage.setFailedPaymentMsg(emailId, orderId);
                    kafkaProducerClient.sendMessage("orderStatus", objectMapper.writeValueAsString(emailMessage));
                    break;
                }
                default:
                    System.out.println("Unhandled event type: " + event);
            }
            return ResponseEntity.ok("Webhook received");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: " + e.getMessage());
        }
    }
}