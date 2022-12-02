package com.example.teampro_product.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServerException extends Exception{
    private ServerResponseStatus status;
}
