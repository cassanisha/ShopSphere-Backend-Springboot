package com.anisha.OrderServiceF.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Order order;

    private Long productId;

    private String size;

    private int quantity;

    private Double price;

    private Double discountedPrice;

    private Long userId;

    private LocalDateTime deliveryDate;


}