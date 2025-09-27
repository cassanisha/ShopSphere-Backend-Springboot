package com.anisha.OrderServiceF.services;

import com.anisha.OrderServiceF.dtos.AddItemRequest;
import com.anisha.OrderServiceF.dtos.ProductDto;
import com.anisha.OrderServiceF.exceptions.CartItemException;
import com.anisha.OrderServiceF.exceptions.ProductException;
import com.anisha.OrderServiceF.models.Cart;
import com.anisha.OrderServiceF.models.CartItem;
import com.anisha.OrderServiceF.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CartServiceImplementation implements CartService{

    private final String productServiceUrl = "http://product-service:8081/products/user/id";
    private CartRepository cartRepository;
    private CartItemService cartItemService;
    @Autowired
    private RestTemplate restTemplate;


    public CartServiceImplementation(CartRepository cartRepository,CartItemService cartItemService, RestTemplate restTemplate
                                   ) {
        this.cartRepository=cartRepository;
        this.cartItemService=cartItemService;
        this.restTemplate=restTemplate;
    }

    @Override
    public Cart createCart(Long userId) {

        Cart cart = new Cart();
        cart.setUserId(userId);
        Cart createdCart=cartRepository.save(cart);
        return createdCart;
    }

    public Cart findUserCart(Long userId) {
        Cart cart =	cartRepository.findByUserId(userId);
        int totalPrice=0;
        int totalDiscountedPrice=0;
        int totalItem=0;
        for(CartItem cartsItem : cart.getCartItems()) {
            totalPrice+=cartsItem.getPrice();
            totalDiscountedPrice+=cartsItem.getDiscountedPrice();
            totalItem+=cartsItem.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalItem(cart.getCartItems().size());
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);

        return cartRepository.save(cart);

    }

    @Override
    public CartItem addCartItem(Long userId, AddItemRequest req) throws ProductException, CartItemException {
        // 1. Get the user's cart, create if null
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = createCart(userId);
        }

        // 2. Get product details
        ProductDto product = restTemplate.getForObject(
                productServiceUrl + "/" + req.getProductId(),
                ProductDto.class
        );
        if (product == null) throw new ProductException("Product not found: " + req.getProductId());

        // 3. Check if item already exists in the cart
        CartItem existingItem = cartItemService.isCartItemExist(cart, req.getProductId(), req.getSize(), userId);

        if (existingItem != null) {
            // 4a. Item exists → update quantity and prices
            int newQuantity = existingItem.getQuantity() + req.getQuantity();
            existingItem.setQuantity(newQuantity);
            existingItem.setPrice(product.getPrice() * newQuantity);
            existingItem.setDiscountedPrice(product.getDiscountedPrice() * newQuantity);

            return cartItemService.updateCartItem(userId, existingItem.getId(), existingItem);

        } else {
            // 4b. Item does not exist → create new CartItem
            CartItem newItem = new CartItem();
            newItem.setProductId(req.getProductId());
            newItem.setCart(cart);
            newItem.setQuantity(req.getQuantity());
            newItem.setUserId(userId);
            newItem.setSize(req.getSize());
            newItem.setPrice(product.getPrice() * req.getQuantity());
            newItem.setDiscountedPrice(product.getDiscountedPrice() * req.getQuantity());

            CartItem createdItem = cartItemService.createCartItem(newItem);
            cart.getCartItems().add(createdItem);
            cartRepository.save(cart);
            return createdItem;
        }
    }

}
