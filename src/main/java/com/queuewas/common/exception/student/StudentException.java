package com.queuewas.common.exception.student;


import com.queuewas.common.exception.GlobalException;

import lombok.Getter;

@Getter
public class StudentException extends GlobalException {

	public StudentException(StudentErrorCode studentErrorCode) {
		super(studentErrorCode);
	}
}
