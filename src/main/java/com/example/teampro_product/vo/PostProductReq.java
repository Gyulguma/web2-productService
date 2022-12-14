package com.example.teampro_product.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostProductReq {
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
}
