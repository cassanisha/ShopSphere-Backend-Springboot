package com.anisha.ProductServiceF.controllers;

import com.anisha.ProductServiceF.exceptions.ProductException;
import com.anisha.ProductServiceF.models.Review;
import com.anisha.ProductServiceF.requests.ReviewRequest;
import com.anisha.ProductServiceF.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/reviews")
public class ReviewController {

    private ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        this.reviewService=reviewService;

    }
    @PostMapping("/create")
    public ResponseEntity<Review> createReviewHandler  (
            @RequestBody ReviewRequest req,
            @RequestHeader("X-User-Id") Long userId) throws ProductException { // <-- The change is here!

        // Now you have the authenticated user's ID, which you can pass to the service.
        Review review = reviewService.createReview( req, userId);

        // Always return a DTO, not the raw entity.
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductsReviewHandler(@PathVariable Long productId){
        List<Review>reviews=reviewService.getAllReview(productId);
        return new ResponseEntity<List<Review>>(reviews,HttpStatus.OK);
    }

}
