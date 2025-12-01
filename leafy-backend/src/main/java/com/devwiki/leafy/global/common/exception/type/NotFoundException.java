package com.devwiki.leafy.global.common.exception.type;

import com.devwiki.leafy.global.common.exception.enums.BadStatusCode;

public class NotFoundException extends CustomException {
    public NotFoundException(BadStatusCode badStatusCode) {
        super(badStatusCode);
    }
}
