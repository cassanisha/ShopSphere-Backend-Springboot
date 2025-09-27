package com.anisha.ProductServiceF.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ProductResponse {

    private Long id;
    private String title;
    private String description;
    private Integer price;
    private Integer discountedPrice;
    private Integer discountPercent;
    private Integer quantity;
    private String brand;
    private String color;
    private String imageUrl;



    // getters and setters (or use Lombok @Data/@Builder)
}
