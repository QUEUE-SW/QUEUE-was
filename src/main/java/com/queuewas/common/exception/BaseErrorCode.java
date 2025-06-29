package com.queuewas.common.exception;

public interface BaseErrorCode {
	ErrorCausedBy errorCausedBy();
	String getErrorMessage();
}
