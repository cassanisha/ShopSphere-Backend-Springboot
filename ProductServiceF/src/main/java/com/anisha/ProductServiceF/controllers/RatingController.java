package com.anisha.ProductServiceF.controllers;

import com.anisha.ProductServiceF.exceptions.ProductException;
import com.anisha.ProductServiceF.models.Rating;
import com.anisha.ProductServiceF.requests.RatingRequest;
import com.anisha.ProductServiceF.services.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/ratings")
public class RatingController {


    private RatingService ratingService;

    public RatingController(RatingService ratingServices) {
        this.ratingService=ratingServices;

        // TODO Auto-generated constructor stub
    }

    @PostMapping("/create")
    public ResponseEntity<Rating> createRatingHandler(
            @RequestBody RatingRequest req,
            @RequestHeader("X-User-Id") Long userId) throws ProductException { // No more UserException

        // The authenticated userId is now passed directly to the service
        Rating rating = ratingService.createRating(req, userId);

        // Always return a DTO and use 201 CREATED for new resources
        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductsReviewHandler(@PathVariable Long productId){

        List<Rating> ratings=ratingService.getProductsRating(productId);
        return new ResponseEntity<>(ratings,HttpStatus.OK);
    }
}
