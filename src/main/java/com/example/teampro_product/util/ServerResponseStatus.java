package com.example.teampro_product.util;

import lombok.Getter;

@Getter
public enum ServerResponseStatus {

    SUCCESS("요청에 성공하였습니다."),
    DATA_OF_NAME_NOT_FOUND("해당 도서를 찾을 수 없습니다."),
    NOT_REGISTERED_BOOK("등록된 도서가 아닙니다."),
    DATABASE_ERROR("데이터베이스 연결에 실패하였습니다."),
    ALREADY_EXIST_BOOK("이미 등록된 도서입니다.");


    private final String message;
    private ServerResponseStatus(String message) {
        this.message = message;
    }
}
