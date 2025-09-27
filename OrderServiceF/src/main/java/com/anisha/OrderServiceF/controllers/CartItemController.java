package com.anisha.OrderServiceF.controllers;


import com.anisha.OrderServiceF.configs.JwtService;
import com.anisha.OrderServiceF.dtos.AddItemRequest;
import com.anisha.OrderServiceF.dtos.ApiResponse;
import com.anisha.OrderServiceF.exceptions.CartItemException;
import com.anisha.OrderServiceF.exceptions.ProductException;
import com.anisha.OrderServiceF.exceptions.UserException;
import com.anisha.OrderServiceF.models.Cart;
import com.anisha.OrderServiceF.models.CartItem;
import com.anisha.OrderServiceF.services.CartItemService;
import com.anisha.OrderServiceF.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order/cart")
public class CartItemController {

    private CartItemService cartItemService;
    private CartService cartService;

    @Autowired
    public CartItemController(CartItemService cartItemService, CartService cartService ) {
        this.cartItemService=cartItemService;
        this.cartService=cartService;
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable Long cartItemId ) throws CartItemException, UserException {


        cartItemService.removeCartItem(JwtService.getUserId(), cartItemId);

        ApiResponse res=new ApiResponse("Item Remove From Cart",true);

        return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItem>updateCartItemHandler(@PathVariable Long cartItemId, @RequestBody CartItem cartItem ) throws CartItemException, UserException{



        CartItem updatedCartItem =cartItemService.updateCartItem(JwtService.getUserId(), cartItemId, cartItem);

        //ApiResponse res=new ApiResponse("Item Updated",true);

        return new ResponseEntity<>(updatedCartItem,HttpStatus.ACCEPTED);
    }
    @GetMapping("/")
    public ResponseEntity<Cart> findUserCartHandler() throws UserException{



        Cart cart=cartService.findUserCart( JwtService.getUserId() );

        System.out.println("cart - "+cart.getUserId() );

        return new ResponseEntity<Cart>(cart,HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req ) throws CartItemException, ProductException {



        CartItem item = cartService.addCartItem(JwtService.getUserId(), req);

        ApiResponse res= new ApiResponse("Item Added To Cart Successfully",true);

        return new ResponseEntity<>(item,HttpStatus.ACCEPTED);

    }
}
