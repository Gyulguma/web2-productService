package com.example.teampro_product.jpa;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    ProductEntity findByName(String name);
    ProductEntity findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
}
