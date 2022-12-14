package com.example.teampro_product.service;

import com.example.teampro_product.dto.ProductDto;
import com.example.teampro_product.jpa.ProductEntity;
import com.example.teampro_product.util.ServerException;
import com.example.teampro_product.vo.RequestUpdateQuantity;

public interface ProductService {
    Iterable<ProductEntity> getAllProducts() throws ServerException;
    ProductDto getProduct(String name) throws ServerException;
    ProductDto addProduct(ProductDto productDetails) throws ServerException;
    ProductDto modifyInfo(String isbn, ProductDto productDetails) throws ServerException;
    boolean deleteProduct(String isbn) throws ServerException;
    Integer getproductComments(String isbn);
    Integer getQuantity(String isbn);
    boolean updateQuantity(RequestUpdateQuantity quantity);
    boolean updateComments(String isbn);
}
