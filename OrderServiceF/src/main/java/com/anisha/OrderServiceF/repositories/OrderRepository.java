package com.anisha.OrderServiceF.repositories;

import java.util.List;
import java.util.Optional;

import com.anisha.OrderServiceF.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.anisha.OrderServiceF.models.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("SELECT o FROM Order o WHERE o.userId = :userId AND (o.orderStatus = com.anisha.OrderServiceF.models.OrderStatus.PLACED OR o.orderStatus = com.anisha.OrderServiceF.models.OrderStatus.CONFIRMED OR o.orderStatus = com.anisha.OrderServiceF.models.OrderStatus.SHIPPED OR o.orderStatus = com.anisha.OrderServiceF.models.OrderStatus.DELIVERED)")

	List<Order> findAllByOrderByCreatedAtDesc();


	Optional<Order>findByOrderId(String orderId);


	@Query("SELECT o FROM Order o WHERE o.userId = :userId")
	List<Order> getUsersOrders(@Param("userId") Long userId);
}
