package com.anisha.ProductServiceF.services;

import com.anisha.ProductServiceF.exceptions.ProductException;
import com.anisha.ProductServiceF.models.Review;
import com.anisha.ProductServiceF.requests.ReviewRequest;

import java.util.List;

public interface ReviewService {

    public Review createReview(ReviewRequest req, Long userId) throws ProductException;

    public List<Review> getAllReview(Long productId);


}