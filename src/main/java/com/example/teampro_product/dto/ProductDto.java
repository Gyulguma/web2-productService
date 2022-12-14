package com.example.teampro_product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductDto implements Serializable {
    private String isbn;
    private String type;
    private String name;
    private String author;
    private String publisher;
    private Date date;
    private Integer pages;
    private String size;
    private Integer weight;
    private String introduction;
    private String tableOfContents;
    private Integer fixedPrice;
    private Integer price;
    private Integer quantity;
    private Integer comments;
}
