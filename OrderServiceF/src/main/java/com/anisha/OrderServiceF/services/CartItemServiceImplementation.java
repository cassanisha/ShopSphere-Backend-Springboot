package com.anisha.OrderServiceF.services;

import java.util.Optional;

import com.anisha.OrderServiceF.configs.JwtService;
import com.anisha.OrderServiceF.dtos.ProductDto;
import com.anisha.OrderServiceF.exceptions.CartItemException;
import com.anisha.OrderServiceF.exceptions.UserException;
import com.anisha.OrderServiceF.models.Cart;
import com.anisha.OrderServiceF.models.CartItem;
import com.anisha.OrderServiceF.repositories.CartItemRepository;
import com.anisha.OrderServiceF.repositories.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class CartItemServiceImplementation implements CartItemService {


	private final CartItemRepository cartItemRepository;
	private final RestTemplate restTemplate;

	private final String productServiceUrl = "http://product-service:8081/products/user/id";

	public CartItemServiceImplementation(CartItemRepository cartItemRepository, RestTemplate restTemplate) {
		this.cartItemRepository = cartItemRepository;
		this.restTemplate = restTemplate;
	}

	@Override
	public CartItem createCartItem(CartItem cartItem) {

		ProductDto product = restTemplate.getForObject(
				productServiceUrl + "/" + cartItem.getProductId(),
				ProductDto.class
		);
		
		cartItem.setQuantity(1);
		cartItem.setProductId(product.getId());
		cartItem.setPrice((product.getPrice()*cartItem.getQuantity()));
		cartItem.setDiscountedPrice( product.getDiscountedPrice()*cartItem.getQuantity() );
		CartItem createdCartItem=cartItemRepository.save(cartItem);
		
		return createdCartItem;
	}

	@Override
	public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) throws CartItemException {

		// 1. Find the existing cart item
		CartItem item = findCartItemById(cartItemId);

		ProductDto product = restTemplate.getForObject(
				productServiceUrl + "/" + item.getProductId(),
				ProductDto.class
		);

		if (product == null) {
			throw new CartItemException("Product not found for id: " + item.getProductId());
		}

		if (!JwtService.getUserId().equals(userId)) {
			throw new CartItemException("You can't update another user's cart item");
		}

		item.setQuantity(cartItem.getQuantity());
		item.setPrice(item.getQuantity() * product.getPrice());
		item.setDiscountedPrice(item.getQuantity() * product.getDiscountedPrice());

		return cartItemRepository.save(item);
	}


	@Override
	public CartItem isCartItemExist(Cart cart, Long productId, String size, Long userId) {
		
		CartItem cartItem=cartItemRepository.isCartItemExist(cart, productId, size, userId);
		
		return cartItem;
	}
	
	

	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
		System.out.println("userId- " + userId + " cartItemId " + cartItemId);

		CartItem cartItem = findCartItemById(cartItemId);

		if (!cartItem.getUserId().equals(userId)) {
			throw new UserException("You can't remove another user's item");
		}

		cartItemRepository.deleteById(cartItem.getId());
	}

	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		Optional<CartItem> opt=cartItemRepository.findById(cartItemId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new CartItemException("cartItem not found with id : "+cartItemId);
	}

}
