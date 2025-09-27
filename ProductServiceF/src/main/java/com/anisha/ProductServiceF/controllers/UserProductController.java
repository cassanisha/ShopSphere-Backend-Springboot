package com.anisha.ProductServiceF.controllers;


import com.anisha.ProductServiceF.exceptions.ProductException;
import com.anisha.ProductServiceF.models.Product;
import com.anisha.ProductServiceF.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/user")
public class UserProductController {

    private ProductService productService;

    public UserProductController(ProductService productService) {
        this.productService=productService;
    }


    @GetMapping("/filter")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> color,
            @RequestParam(required = false) List<String> size,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false, defaultValue = "createdAt") String sort,
            @RequestParam(required = false, defaultValue = "all") String stock,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize
    ) {
        Page<Product> res = productService.getAllProduct(
                category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize
        );

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }


    @GetMapping("/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {

        Product product=productService.findProductById(productId);

        return new ResponseEntity<Product>(product,HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String q){

        List<Product> products=productService.searchProduct(q);

        return new ResponseEntity<List<Product>>(products,HttpStatus.OK);

    }
}