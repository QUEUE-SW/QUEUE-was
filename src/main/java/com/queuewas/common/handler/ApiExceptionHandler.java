package com.queuewas.common.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.queuewas.common.exception.GlobalException;
import com.queuewas.common.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<?> handleGlobalException(GlobalException globalException) {
		log.warn("exception = {}, error code = {}", globalException.getBaseErrorCode(),
			globalException.errorCausedBy().getErrorCode());
		return ResponseEntity.status(globalException.errorCausedBy().statusCode().getCode())
			.body(ErrorResponse.of(
				globalException.errorCausedBy().getErrorCode(),
				globalException.getErrorMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getFieldErrors().stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.findFirst()
			.orElse("잘못된 입력입니다.");

		return ResponseEntity.badRequest().body(ErrorResponse.of("4000", errorMessage));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleInvalidJson(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest().body(ErrorResponse.of("4001", "잘못된 요청 형식입니다."));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
		return ResponseEntity.badRequest().body(ErrorResponse.of("4002", "필수 요청 파라미터가 없습니다: " + ex.getParameterName()));
	}
}
