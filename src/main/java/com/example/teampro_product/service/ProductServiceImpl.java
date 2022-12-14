package com.example.teampro_product.service;

import com.example.teampro_product.dto.ProductDto;
import com.example.teampro_product.jpa.ProductEntity;
import com.example.teampro_product.jpa.ProductRepository;
import com.example.teampro_product.util.ServerException;
import com.example.teampro_product.util.ServerResponseStatus;
import com.example.teampro_product.vo.RequestUpdateQuantity;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Server;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
    ProductRepository productRepository;
    Environment env;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, Environment env){
        this.productRepository = productRepository;
        this.env = env;
    }
    @Override
    public Iterable<ProductEntity> getAllProducts() throws ServerException{
        try {
            return productRepository.findAll();
        }catch (Exception e){
            throw new ServerException(ServerResponseStatus.DATABASE_ERROR);
        }
    }

    @Override
    public ProductDto getProduct(String name) throws ServerException{
        ProductEntity productEntity = this.productRepository.findByName(name);
        if(productEntity == null) throw new ServerException(ServerResponseStatus.DATA_OF_NAME_NOT_FOUND);
        ProductDto productDto = new ModelMapper().map(productEntity, ProductDto.class);
        return productDto;
    }

    @Override
    public ProductDto addProduct(ProductDto productDetails) throws ServerException {
        if(this.productRepository.existsByIsbn(productDetails.getIsbn())) throw new ServerException(ServerResponseStatus.ALREADY_EXIST_BOOK);

        try{
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            productDetails.setComments(0);
            ProductEntity productEntity = modelMapper.map(productDetails, ProductEntity.class);

            this.productRepository.save(productEntity);
            ProductEntity checkEntity = this.productRepository.findByIsbn(productDetails.getIsbn());
            ProductDto resultValue = modelMapper.map(checkEntity, ProductDto.class);
            return resultValue;
        }catch (Exception e){
            throw new ServerException(ServerResponseStatus.DATABASE_ERROR);
        }
    }

    @Override
    public ProductDto modifyInfo(String isbn, ProductDto productDetails) throws ServerException {
        if(!(this.productRepository.existsByIsbn(isbn))) throw new ServerException(ServerResponseStatus.NOT_REGISTERED_BOOK);

        try{
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

            ProductEntity fixedDetails = modelMapper.map(productDetails, ProductEntity.class);
            ProductEntity originalDetails = this.productRepository.findByIsbn(isbn);
            originalDetails.setIsbn(fixedDetails.getIsbn());
            originalDetails.setType(fixedDetails.getType());
            originalDetails.setName(fixedDetails.getName());
            originalDetails.setAuthor(fixedDetails.getAuthor());
            originalDetails.setPublisher(fixedDetails.getPublisher());
            originalDetails.setDate(fixedDetails.getDate());
            originalDetails.setPages(fixedDetails.getPages());
            originalDetails.setSize(fixedDetails.getSize());
            originalDetails.setWeight(fixedDetails.getWeight());
            originalDetails.setIntroduction(fixedDetails.getIntroduction());
            originalDetails.setTableOfContents(fixedDetails.getTableOfContents());
            originalDetails.setFixedPrice(fixedDetails.getFixedPrice());
            originalDetails.setPrice(fixedDetails.getPrice());
            originalDetails.setQuantity(fixedDetails.getQuantity());
            this.productRepository.save(originalDetails);
            ProductDto resultValue = modelMapper.map(originalDetails, ProductDto.class);
            return resultValue;
        }catch (Exception e){
            throw new ServerException(ServerResponseStatus.DATABASE_ERROR);
        }
    }

    @Override
    public boolean deleteProduct(String isbn) throws ServerException {
        if(!(this.productRepository.existsByIsbn(isbn))) throw new ServerException(ServerResponseStatus.NOT_REGISTERED_BOOK);
        try{
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            ProductEntity productEntity = this.productRepository.findByIsbn(isbn);
            this.productRepository.delete(productEntity);
            boolean result = !(this.productRepository.existsByIsbn(isbn));
            return result;
        }catch (Exception e){
            throw new ServerException(ServerResponseStatus.DATABASE_ERROR);
        }
    }

    @Override
    public Integer getproductComments(String isbn) {
        if(!(this.productRepository.existsByIsbn(isbn))) return 0;

        ProductEntity productEntity = this.productRepository.findByIsbn(isbn);
        Integer result = productEntity.getComments();
        return result;
    }
    @Override
    public Integer getQuantity(String isbn) {
        if(!(this.productRepository.existsByIsbn(isbn))) return 0;

        ProductEntity productEntity = this.productRepository.findByIsbn(isbn);
        Integer result = productEntity.getQuantity();
        return result;
    }
    @Override
    public boolean updateQuantity(RequestUpdateQuantity quantity) {
        if(!(this.productRepository.existsByIsbn(quantity.getIsbn()))) return false;

        ProductEntity productEntity = this.productRepository.findByIsbn(quantity.getIsbn());
        Integer updateQuantity = productEntity.getQuantity()-quantity.getQuantity();
        productEntity.setQuantity(updateQuantity);
        this.productRepository.save(productEntity);

        ProductEntity updatedProduct = this.productRepository.findByIsbn(quantity.getIsbn());
        return updatedProduct.getQuantity() == updateQuantity;
    }
    @Override
    public boolean updateComments(String isbn) {
        if(!(this.productRepository.existsByIsbn(isbn))) return false;

        ProductEntity productEntity = this.productRepository.findByIsbn(isbn);
        Integer updateComments = productEntity.getComments()+1;
        productEntity.setComments(updateComments);
        this.productRepository.save(productEntity);

        ProductEntity updatedProduct = this.productRepository.findByIsbn(isbn);
        return updatedProduct.getComments() == updateComments;
    }

}
