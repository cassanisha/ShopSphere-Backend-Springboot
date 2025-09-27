package com.anisha.OrderServiceF.services;

import com.anisha.OrderServiceF.models.OrderItem;
import com.anisha.OrderServiceF.repositories.OrderItemRepository;
import org.springframework.stereotype.Service;


@Service
public class OrderItemServiceImplementation implements OrderItemService {

	private OrderItemRepository orderItemRepository;
	public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {
		this.orderItemRepository=orderItemRepository;
	}
	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {
		
		return orderItemRepository.save(orderItem);
	}

}
