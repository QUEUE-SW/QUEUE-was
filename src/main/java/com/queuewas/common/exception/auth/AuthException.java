package com.queuewas.common.exception.auth;

import com.queuewas.common.exception.GlobalException;

public class AuthException extends GlobalException {

	public AuthException(AuthErrorCode authErrorCode) {
		super(authErrorCode);
	}
}
