package com.anisha.ProductServiceF.controllers;

import com.anisha.ProductServiceF.exceptions.ProductException;
import com.anisha.ProductServiceF.models.Product;
import com.anisha.ProductServiceF.requests.CreateProductRequest;
import com.anisha.ProductServiceF.responses.ApiResponse;
import com.anisha.ProductServiceF.responses.ProductResponse;
import com.anisha.ProductServiceF.services.ProductService;
import com.anisha.ProductServiceF.services.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/admin")
public class AdminProductController {

    private ProductService productService;

    public AdminProductController(ProductServiceImpl  productService) {
        this.productService = productService;
    }

    public static ProductResponse toProductResponse(Product product) {
        if (product == null) return null;

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setDiscountedPrice(product.getDiscountedPrice());
        response.setDiscountPercent(product.getDiscountPercent());
        response.setQuantity(product.getQuantity());
        response.setBrand(product.getBrand());
        response.setColor(product.getColor());
        response.setImageUrl(product.getImageUrl());
        return response;
    }
    @PostMapping("/creates")
    public ResponseEntity<ProductResponse> createProductHandler(@RequestBody CreateProductRequest req) throws ProductException {

        Product createdProduct = productService.createProduct(req);

        return ResponseEntity.ok(AdminProductController.toProductResponse(createdProduct));

    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProductHandler(@PathVariable Long productId) throws ProductException{

        System.out.println("delete product controller .... ");
        String msg=productService.deleteProduct(productId);
        System.out.println("delete product controller .... msg "+msg);
        ApiResponse res=new ApiResponse( msg,true);

        return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);

    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAllProduct(){

        List<Product> products = productService.getAllProducts();

        return new ResponseEntity<List<Product>>(products,HttpStatus.OK);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Product>> recentlyAddedProduct(){

        List<Product> products = productService.recentlyAddedProduct();

        return new ResponseEntity<List<Product>>(products,HttpStatus.OK);
    }


    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProductHandler(@RequestBody Product req,@PathVariable Long productId) throws ProductException{

        Product updatedProduct=productService.updateProduct(productId, req);

        return new ResponseEntity<Product>(updatedProduct,HttpStatus.OK);
    }

    @PostMapping("/creates/multiple")
    public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody CreateProductRequest[] reqs) throws ProductException{

        for(CreateProductRequest product:reqs) {
            productService.createProduct(product);
        }

        ApiResponse res=new ApiResponse("products created successfully",true);
        return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
    }

}
