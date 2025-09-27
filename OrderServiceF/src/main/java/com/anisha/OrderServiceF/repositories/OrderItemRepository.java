package com.anisha.OrderServiceF.repositories;

import com.anisha.OrderServiceF.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
