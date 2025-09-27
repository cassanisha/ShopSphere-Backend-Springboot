package com.anisha.ProductServiceF.services;

import com.anisha.ProductServiceF.exceptions.ProductException;
import com.anisha.ProductServiceF.models.Rating;
import com.anisha.ProductServiceF.requests.RatingRequest;

import java.util.List;

public interface RatingService {

    public Rating createRating(RatingRequest req, Long userId ) throws ProductException;

    public List<Rating> getProductsRating(Long productId);

}
