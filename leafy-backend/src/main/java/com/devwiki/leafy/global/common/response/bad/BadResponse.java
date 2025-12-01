package com.devwiki.leafy.global.common.response.bad;

import org.springframework.http.HttpStatus;

import com.devwiki.leafy.global.common.exception.enums.BadStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BadResponse {
    private final int code;
    private final String message;

    //우리가 정한 예외 응답
    public static BadResponse makeResponse(BadStatusCode badStatusCode){
        return new BadResponse(badStatusCode.getHttpStatus().value(),badStatusCode.getMessage());
    }

    //우리가 정한것 외 응답 (500 에러로 )
    public static BadResponse make5xxResponse(String message) {
        return new BadResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    //우리가 정한것 외 응답 (400 에러로 )
    public static BadResponse make4xxResponse(String message){
        return new BadResponse(HttpStatus.BAD_REQUEST.value(), message);
    }

}
