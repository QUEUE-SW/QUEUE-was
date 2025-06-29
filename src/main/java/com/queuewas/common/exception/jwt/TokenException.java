package com.queuewas.common.exception.jwt;

import com.queuewas.common.exception.GlobalException;

public class TokenException extends GlobalException {

	public TokenException(TokenErrorCode tokenErrorCode) {
		super(tokenErrorCode);
	}
}
