package com.devwiki.leafy.global.common.exception.type;

import com.devwiki.leafy.global.common.exception.enums.BadStatusCode;

public class ServerErrorException extends CustomException {
    public ServerErrorException(BadStatusCode badStatusCode) {
        super(badStatusCode);
    }
}
