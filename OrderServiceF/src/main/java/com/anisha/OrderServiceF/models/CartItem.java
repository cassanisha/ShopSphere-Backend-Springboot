package com.anisha.OrderServiceF.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Cart cart;

    private Long productId;

    private String size;

    private int quantity;

    private Double price;

    private Double discountedPrice;

    private Long userId;

}