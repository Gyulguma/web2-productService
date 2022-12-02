package com.example.teampro_product.controller;

import com.example.teampro_product.dto.ProductDto;
import com.example.teampro_product.jpa.ProductEntity;
import com.example.teampro_product.service.ProductService;
import com.example.teampro_product.util.CommonResponse;
import com.example.teampro_product.util.ServerException;
import com.example.teampro_product.util.ServerResponseStatus;
import com.example.teampro_product.vo.GetProductRes;
import com.example.teampro_product.vo.PostProductReq;
import com.example.teampro_product.vo.PostProductRes;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.Server;
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
@RequestMapping("/product-service/products")
public class ProductController {

    private Environment env;
    ProductService productService;
    @Autowired
    public ProductController(Environment env, ProductService productService){
        this.env = env;
        this.productService = productService;
    }

    @GetMapping("/health-check")
    public String status(HttpServletRequest request){
        return String.format("It's Working in Product Service on Port %s", request.getServerPort());
    }

    @GetMapping("/getallproduct")
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

    @GetMapping("/getproduct")
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
    public ResponseEntity<CommonResponse<PostProductRes>> addProduct(@PathVariable("isbn") String isbn, @RequestBody PostProductReq productDetails){
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
}
