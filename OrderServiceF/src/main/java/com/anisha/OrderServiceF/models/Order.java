package com.anisha.OrderServiceF.models;


import com.anisha.OrderServiceF.dtos.OrderDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDate orderDate = LocalDate.now();

    private LocalDate deliveryDate;

    @OneToOne
    private Address shippingAddress;

    private double totalPrice;

    private Double totalDiscountedPrice;

    private Double discount;

    private OrderStatus orderStatus;

    private int totalItem;

    private int url;

    private LocalDateTime createdAt;

    public OrderDto toOrderDto() {
        OrderDto dto = new OrderDto();
        dto.setId(this.getId());
        dto.setTotalAmount(this.getTotalPrice());
        dto.setStatus(this.getOrderStatus());
        dto.setUserId(this.getUserId());
        dto.setOrderId(this.orderId);
        // Map order items
        List<Long> productIds = new ArrayList<>();
        for (OrderItem item : this.getOrderItems()) {
            productIds.add(item.getProductId());
        }
        dto.setProductIds(productIds);
        return dto;
    }

}
