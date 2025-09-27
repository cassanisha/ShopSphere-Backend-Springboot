package com.anisha.ProductServiceF.models;


import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class Size implements Serializable {

    private String name;
    private int quantity;

}
