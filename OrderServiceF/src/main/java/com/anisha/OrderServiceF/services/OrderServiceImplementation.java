package com.anisha.OrderServiceF.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.anisha.OrderServiceF.dtos.OrderDto;
import com.anisha.OrderServiceF.exceptions.OrderException;
import com.anisha.OrderServiceF.models.*;
import com.anisha.OrderServiceF.repositories.AddressRepository;
import com.anisha.OrderServiceF.repositories.OrderItemRepository;
import com.anisha.OrderServiceF.repositories.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImplementation implements OrderService {
	
	private OrderRepository orderRepository;
	private CartService cartService;
	private AddressRepository addressRepository;

	private OrderItemService orderItemService;
	private OrderItemRepository orderItemRepository;
	private ObjectMapper objectMapper;
	
	public OrderServiceImplementation(OrderRepository orderRepository,CartService cartService,
			AddressRepository addressRepository,
			OrderItemService orderItemService,OrderItemRepository orderItemRepository,
			ObjectMapper objectMapper) {
		this.orderRepository=orderRepository;
		this.cartService=cartService;
		this.addressRepository=addressRepository;
		this.orderItemService=orderItemService;
		this.orderItemRepository=orderItemRepository;
		this.objectMapper=objectMapper;
	}

	@Override
	public OrderDto createOrder(Long userId, String Email, Address shippAddress) {

		shippAddress.setUserId(userId);
		Address address = addressRepository.save(shippAddress);


		Cart cart=cartService.findUserCart(userId);
		List<OrderItem> orderItems=new ArrayList<>();

		for(CartItem item: cart.getCartItems()) {
			OrderItem orderItem=new OrderItem();

			orderItem.setPrice(item.getPrice());
			orderItem.setProductId(item.getProductId());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setSize(item.getSize());
			orderItem.setUserId(item.getUserId());
			orderItem.setDiscountedPrice(item.getDiscountedPrice());


			OrderItem createdOrderItem=orderItemRepository.save(orderItem);

			orderItems.add(createdOrderItem);
		}


		Order createdOrder=new Order();
		createdOrder.setOrderId(UUID.randomUUID().toString());
		createdOrder.setUserId(userId);
		createdOrder.setOrderItems(orderItems);
		createdOrder.setTotalPrice(cart.getTotalPrice());
		createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
		createdOrder.setDiscount(cart.getTotalPrice() - cart.getTotalDiscountedPrice());
		createdOrder.setTotalItem(cart.getTotalItem());

		createdOrder.setShippingAddress(address);
		createdOrder.setOrderDate(LocalDate.now());
		createdOrder.setOrderStatus(OrderStatus.PENDING);
		createdOrder.setCreatedAt(LocalDateTime.now());

		Order savedOrder=orderRepository.save(createdOrder);

		for(OrderItem item:orderItems) {
			item.setOrder(savedOrder);
			orderItemRepository.save(item);

		}

		OrderDto customer_order= createdOrder.toOrderDto();
		customer_order.setEmail(Email);

		return customer_order;
	}



	@Override
	public OrderDto placedOrder(String orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.PLACED);

		return order.toOrderDto();
	}
	//method not used yet
	@Override
	public Order confirmedOrder(String orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.CONFIRMED);
		
		
		return orderRepository.save(order);
	}
	//method not used yet
	@Override
	public Order shippedOrder(String orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.SHIPPED);
		return orderRepository.save(order);
	}
	//method not used yet
	@Override
	public Order deliveredOrder(String orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.DELIVERED);
		return orderRepository.save(order);
	}

	//method not used yet
	@Override
	public OrderDto cancledOrder(String orderId) throws OrderException {
		Order order=findOrderById(orderId);
		order.setOrderStatus(OrderStatus.CANCELLED);
		return orderRepository.save(order).toOrderDto();
	}

	@Override
	public Order findOrderById(String orderId) throws OrderException {
		Optional<Order> opt=orderRepository.findByOrderId(orderId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new OrderException("order not exist with id "+orderId);
	}

	@Override
	public List<Order> usersOrderHistory(Long userId) {
		List<Order> orders=orderRepository.getUsersOrders(userId);
		return orders;
	}

	@Override
	public List<Order> getAllOrders() {
		
		return orderRepository.findAllByOrderByCreatedAtDesc();
	}



}
