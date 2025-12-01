package com.devwiki.leafy.global.common.exception.type;

import com.devwiki.leafy.global.common.exception.enums.BadStatusCode;

public class JwtAuthenticationException extends CustomException {
	public JwtAuthenticationException(BadStatusCode badStatusCode) {
		super(badStatusCode);
	}

}
