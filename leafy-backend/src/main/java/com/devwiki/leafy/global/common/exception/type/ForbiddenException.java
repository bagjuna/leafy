package com.devwiki.leafy.global.common.exception.type;

import com.devwiki.leafy.global.common.exception.enums.BadStatusCode;

public class ForbiddenException extends CustomException {
    public ForbiddenException(BadStatusCode badStatusCode) {
        super(badStatusCode);
    }
}
