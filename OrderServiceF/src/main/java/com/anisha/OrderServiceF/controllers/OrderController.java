package com.anisha.OrderServiceF.controllers;


import com.anisha.OrderServiceF.configs.JwtService;
import com.anisha.OrderServiceF.dtos.OrderDto;
import com.anisha.OrderServiceF.exceptions.OrderException;
import com.anisha.OrderServiceF.exceptions.UserException;
import com.anisha.OrderServiceF.models.Address;
import com.anisha.OrderServiceF.models.Order;
import com.anisha.OrderServiceF.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;


    public OrderController( OrderService orderService ) {
        this.orderService = orderService;
    }

    @PostMapping("/create_order")
    public ResponseEntity<OrderDto> createOrderHandler(
            @RequestBody Address shippingAddress ) throws UserException {


        OrderDto order = orderService.createOrder( JwtService.getUserId() , JwtService.getEmail(),  shippingAddress);

        return new ResponseEntity<OrderDto>(order, HttpStatus.OK);

    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistoryHandler() throws OrderException, UserException {


        List<Order> orders = orderService.usersOrderHistory( JwtService.getUserId() );
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);

    }


    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('SCOPE_order.read')")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable String orderId) throws OrderException, UserException {
        Order order = orderService.findOrderById( orderId );

        OrderDto dto = order.toOrderDto();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/place")
    @PreAuthorize("hasAuthority('SCOPE_order.read')")
    public OrderDto placeOrder(@PathVariable String orderId) throws OrderException {
        return orderService.placedOrder(orderId);
    }

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasAuthority('SCOPE_order.read')")
    public OrderDto cancelOrder(@PathVariable String orderId) throws OrderException {
        return orderService.cancledOrder(orderId);
    }



}