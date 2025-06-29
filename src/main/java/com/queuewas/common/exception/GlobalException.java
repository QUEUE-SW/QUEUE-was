package com.queuewas.common.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

		private final BaseErrorCode baseErrorCode;

		public GlobalException(BaseErrorCode baseErrorCode) {
			this.baseErrorCode = baseErrorCode;
		}

		public ErrorCausedBy errorCausedBy() {
			return baseErrorCode.errorCausedBy();
		}

		public String getErrorMessage() {
			return baseErrorCode.getErrorMessage();
		}
}
