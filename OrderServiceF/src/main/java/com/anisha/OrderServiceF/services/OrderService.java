package com.anisha.OrderServiceF.services;

import com.anisha.OrderServiceF.dtos.OrderDto;
import com.anisha.OrderServiceF.exceptions.OrderException;
import com.anisha.OrderServiceF.models.Address;
import com.anisha.OrderServiceF.models.Order;

import java.util.List;



public interface OrderService {
	
	public OrderDto createOrder(Long userId, String email, Address shippingAdress);
	
	public Order findOrderById(String orderId) throws OrderException;
	
	public List<Order> usersOrderHistory(Long userId);
	
	public OrderDto placedOrder(String orderId) throws OrderException;
	
	public Order confirmedOrder(String orderId)throws OrderException;
	
	public Order shippedOrder(String orderId) throws OrderException;
	
	public Order deliveredOrder(String orderId) throws OrderException;

	public OrderDto cancledOrder(String orderId) throws OrderException;

	public List<Order>getAllOrders();
	

}
