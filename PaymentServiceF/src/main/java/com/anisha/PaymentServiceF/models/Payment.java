package com.anisha.PaymentServiceF.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends BaseModel{

    private Double amount;
    private String orderId;
    private String email;
    private PaymentStatus paymentStatus;
    private PaymentGateway paymentGateway;
    private LocalDateTime paymentDate;

}