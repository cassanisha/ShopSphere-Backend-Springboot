package com.anisha.OrderServiceF.configs;

import com.anisha.OrderServiceF.dtos.PaymentLinkResponseDto;
import com.anisha.OrderServiceF.models.Order;
import com.anisha.OrderServiceF.models.OrderStatus;
import com.anisha.OrderServiceF.repositories.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumerClient {
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;

    public KafkaConsumerClient(ObjectMapper objectMapper, OrderRepository orderRepository) {
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
    }

    @KafkaListener( topics =  "orderServiceStatus", groupId = "orderService")
    public void handlePaymentLink ( String message ){
        try {
            PaymentLinkResponseDto messageDto = objectMapper.readValue(message, PaymentLinkResponseDto.class);
            String orderId= messageDto.getOrderId();
            String status= messageDto.getOrderStatus();

            Optional<Order> optionalOrder = orderRepository.findByOrderId(orderId);
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                if ("Success".equalsIgnoreCase(status)) {
                    order.setOrderStatus(OrderStatus.CONFIRMED);
                } else if( "Failure ".equalsIgnoreCase(status)){
                    order.setOrderStatus(OrderStatus.PAYMENT_FAILED);
                }
                orderRepository.save(order);
            } else {
                System.out.println("Order not found for orderId: " + orderId);
            }

        } catch ( JsonProcessingException e) {
            System.out.println("Failed to parse message: " + message);
            e.printStackTrace();
        }
    }


}
