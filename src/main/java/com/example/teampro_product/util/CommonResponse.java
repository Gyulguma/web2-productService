package com.example.teampro_product.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.teampro_product.util.ServerResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"message", "result"})
public class CommonResponse<T> {

    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 요청에 성공한 경우
    public CommonResponse(T result) {
        this.message = SUCCESS.getMessage();
        this.result = result;
    }

    // 요청에 실패한 경우
    public CommonResponse(ServerResponseStatus status) {
        this.message = status.getMessage();
    }
}
