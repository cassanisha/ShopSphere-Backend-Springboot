package com.anisha.OrderServiceF.repositories;

import com.anisha.OrderServiceF.models.Cart;
import com.anisha.OrderServiceF.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	@Query("SELECT ci From CartItem ci Where ci.cart=:cart And ci.productId=:productId And ci.size=:size And ci.userId=:userId")
	public CartItem isCartItemExist(@Param("cart") Cart cart, @Param("productId")Long productId, @Param("size")String size, @Param("userId")Long userId);
	
}
