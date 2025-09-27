package com.anisha.OrderServiceF.services;


import com.anisha.OrderServiceF.dtos.AddItemRequest;
import com.anisha.OrderServiceF.exceptions.CartItemException;
import com.anisha.OrderServiceF.exceptions.ProductException;
import com.anisha.OrderServiceF.models.Cart;
import com.anisha.OrderServiceF.models.CartItem;

public interface CartService {

    public Cart createCart(Long userId);

    public CartItem addCartItem(Long userId, AddItemRequest req) throws ProductException, CartItemException;

    public Cart findUserCart(Long userId);

}
