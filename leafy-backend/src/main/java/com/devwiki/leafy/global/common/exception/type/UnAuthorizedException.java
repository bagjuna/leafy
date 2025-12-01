package com.devwiki.leafy.global.common.exception.type;

import com.devwiki.leafy.global.common.exception.enums.BadStatusCode;

public class UnAuthorizedException extends CustomException {
    public UnAuthorizedException(BadStatusCode badStatusCode) {
        super(badStatusCode);
    }
}
