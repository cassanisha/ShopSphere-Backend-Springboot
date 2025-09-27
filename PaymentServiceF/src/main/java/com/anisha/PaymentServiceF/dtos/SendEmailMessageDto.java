package com.anisha.PaymentServiceF.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailMessageDto {
    private String from;
    private String to;
    private String subject;
    private String body;

    public SendEmailMessageDto setSuccessfulPaymentMsg (String emailId, String orderId){
        this.setTo(emailId);
        //hardcoded.
        this.setFrom("anishadhillon08@gmail.com");
        this.setSubject("ThankYou for Choosing Us!");
        this.setBody("Your Payment has been successful. Your order with orderID: " + orderId +" is confirmed.");
        return this;
    }
    public SendEmailMessageDto setFailedPaymentMsg (String emailId, String orderId){
        this.setTo(emailId);
        //hardcoded.
        this.setFrom("anishadhillon08@gmail.com");
        this.setSubject("Oops, Payment didn't happen!");
        this.setBody("Your Payment has failed. Your order with orderID: " + orderId + " stands not cancelled. Please try again. ");
        return this;
    }
}