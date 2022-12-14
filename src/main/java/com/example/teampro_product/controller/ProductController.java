package com.example.teampro_product.controller;

import com.example.teampro_product.dto.ProductDto;
import com.example.teampro_product.jpa.ProductEntity;
import com.example.teampro_product.service.ProductService;
import com.example.teampro_product.util.CommonResponse;
import com.example.teampro_product.util.ServerException;
import com.example.teampro_product.util.ServerResponseStatus;
import com.example.teampro_product.vo.*;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/product-service/products")
public class ProductController {

    private Environment env;
    ProductService productService;
    @Autowired
    public ProductController(Environment env, ProductService productService){
        this.env = env;
        this.productService = productService;
    }

    @GetMapping(value = "/health-check")
    public String status(HttpServletRequest request){
        return String.format("It's Working in Product Service on Port %s", request.getServerPort());
    }

    @GetMapping(value = "/getallproduct")
    public ResponseEntity<CommonResponse<List<GetProductRes>>> getAllProduct(){
        try {
            Iterable<ProductEntity> orderList = productService.getAllProducts();

            List<GetProductRes> result = new ArrayList<>();
            orderList.forEach(v -> {
                result.add(new ModelMapper().map(v, GetProductRes.class));
            });
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(result));
        }catch (ServerException e){
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(e.getStatus()));
        }
    }

    @GetMapping(value = "/getproduct")
    public ResponseEntity<CommonResponse<GetProductRes>> getProduct(@RequestParam("name") String name){
        try {
            ProductDto product = productService.getProduct(name);
            GetProductRes result = new ModelMapper().map(product, GetProductRes.class);
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(result));
        } catch (ServerException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(e.getStatus()));
        }
    }

    @PostMapping(value = "/addproduct")
    public ResponseEntity<CommonResponse<PostProductRes>> addProduct(@RequestBody PostProductReq productDetails){
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

            ProductDto productDto = modelMapper.map(productDetails, ProductDto.class);
            ProductDto createDto = this.productService.addProduct(productDto);
            PostProductRes returnValue = modelMapper.map(createDto, PostProductRes.class);

            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(returnValue));
        } catch (ServerException e){
            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(e.getStatus()));
        }
    }

    @PostMapping(value = "/modifyInfo/{isbn}")
    public ResponseEntity<CommonResponse<PostProductRes>> modifyInfo(@PathVariable("isbn") String isbn, @RequestBody PostProductReq productDetails){
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

            ProductDto productDto = modelMapper.map(productDetails, ProductDto.class);
            ProductDto createDto = this.productService.modifyInfo(isbn, productDto);
            PostProductRes returnValue = modelMapper.map(createDto, PostProductRes.class);

            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(returnValue));
        } catch (ServerException e){
            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(e.getStatus()));
        }
    }

    @DeleteMapping(value = "/deleteInfo/{isbn}")
    public ResponseEntity<CommonResponse<ServerResponseStatus>> deleteProduct(@PathVariable("isbn") String isbn){
        try {
            boolean createDto = this.productService.deleteProduct(isbn);
            if(createDto)
                return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(ServerResponseStatus.SUCCESS));
            throw new ServerException(ServerResponseStatus.DATABASE_ERROR);
        } catch (ServerException e){
            return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(e.getStatus()));
        }
    }

    @GetMapping(value = "/getCommentQuantity/{isbn}")
    public Integer getCommentQuantity(@PathVariable("isbn") String isbn){
        Integer result = productService.getproductComments(isbn);
        return result;
    }

    @GetMapping(value = "/getQuantity/{isbn}")
    public ResponseEntity<ResponseQuantity> getQuantity(@PathVariable("isbn") String isbn){
        Integer quantity = productService.getQuantity(isbn);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ResponseQuantity result = new ResponseQuantity();
        result.setQuantity(quantity);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping(value = "/updateQuantity")
    public ResponseEntity<ResponseUpdateQuantity> updateQuantity(@RequestBody RequestUpdateQuantity quantity){
        boolean isSuccess = productService.updateQuantity(quantity);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ResponseUpdateQuantity result = new ResponseUpdateQuantity();
        result.setSuccess(isSuccess);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping(value = "/updateComments/{isbn}")
    public ResponseEntity<ResponseUpdateComments> updateComments(@PathVariable("isbn") String isbn){
        boolean isSuccess = productService.updateComments(isbn);
        ResponseUpdateComments result = new ResponseUpdateComments();
        result.setSuccess(isSuccess);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
