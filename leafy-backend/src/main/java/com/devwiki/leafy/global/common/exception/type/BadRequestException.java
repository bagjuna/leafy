package com.devwiki.leafy.global.common.exception.type;

import com.devwiki.leafy.global.common.exception.enums.BadStatusCode;

public class BadRequestException extends CustomException {
    public BadRequestException(BadStatusCode badStatusCode) {
        super(badStatusCode);
    }
}
