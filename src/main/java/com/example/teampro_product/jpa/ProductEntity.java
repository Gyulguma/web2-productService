package com.example.teampro_product.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "product")
public class ProductEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productIdx;

    @Column(nullable = false, length = 13)
    private String isbn;

    @Column(nullable = false, length = 10)
    private String type;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String author;

    @Column(nullable = false, length = 50)
    private String publisher;

    @Column(nullable = false, length = 8)
    private Date date;

    @Column(nullable = false)
    private Integer pages;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private Integer weight;

    @Column(nullable = false, length = 500)
    private String introduction;

    @Column(nullable = false, length = 100)
    private String tableOfContents;

    @Column(nullable = false, length = 7)
    private Integer fixedPrice;

    @Column(nullable = false, length = 7)
    private Integer price;

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createdAt;

    @Column(nullable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date updatedAt;
}
