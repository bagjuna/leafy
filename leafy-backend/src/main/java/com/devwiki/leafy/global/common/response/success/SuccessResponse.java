package com.devwiki.leafy.global.common.response.success;

import com.devwiki.leafy.global.common.exception.enums.SuccessStatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> {
    private final int code;
    private final String message;
    private final T data;


    //Data 있는 경우
    public static <T> SuccessResponse<T> makeResponse (SuccessStatusCode successStatusCode, T data){
       return new SuccessResponse<>(successStatusCode.getHttpStatus().value(), successStatusCode.getMessage(),data );
    }

    // 데이터가 없는 경우
    public static SuccessResponse<Void> makeResponse(SuccessStatusCode successStatusCode) {
        return new SuccessResponse<>(successStatusCode.getHttpStatus().value(), successStatusCode.getMessage(), null);
    }

}
