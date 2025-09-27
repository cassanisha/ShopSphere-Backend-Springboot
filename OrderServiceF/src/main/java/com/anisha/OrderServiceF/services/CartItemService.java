package com.anisha.OrderServiceF.services;


import com.anisha.OrderServiceF.exceptions.CartItemException;
import com.anisha.OrderServiceF.exceptions.UserException;
import com.anisha.OrderServiceF.models.Cart;
import com.anisha.OrderServiceF.models.CartItem;

public interface CartItemService {
	
	public CartItem createCartItem(CartItem cartItem);
	
	public CartItem updateCartItem(Long userId, Long id,CartItem cartItem) throws CartItemException;
	
	public CartItem isCartItemExist(Cart cart, Long productId, String size, Long userId);
	
	public void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException;
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;

}
