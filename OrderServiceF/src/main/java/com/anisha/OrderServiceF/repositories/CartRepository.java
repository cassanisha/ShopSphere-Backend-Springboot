package com.anisha.OrderServiceF.repositories;

import com.anisha.OrderServiceF.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface CartRepository extends JpaRepository<Cart,Long>{

	@Query("SELECT c From Cart c where c.userId=:userId")
	public Cart findByUserId(@Param("userId")Long userId);
}
