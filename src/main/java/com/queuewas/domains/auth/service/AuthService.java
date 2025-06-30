package com.queuewas.domains.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.queuewas.common.exception.auth.AuthErrorCode;
import com.queuewas.common.exception.auth.AuthException;
import com.queuewas.common.exception.student.StudentErrorCode;
import com.queuewas.common.exception.student.StudentException;
import com.queuewas.domains.auth.dto.request.SignInReq;
import com.queuewas.domains.auth.dto.response.SignInRes;
import com.queuewas.domains.auth.implement.TokenGenerator;
import com.queuewas.domains.student.domain.Student;
import com.queuewas.domains.student.implement.StudentReader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final StudentReader studentReader;
	private final TokenGenerator tokenGenerator;

	public SignInRes signIn(SignInReq signInReq) {
		log.debug("SignInReq: {}", signInReq);
		Student student = studentReader.readByIdentifier(signInReq.identifier())
			.orElseThrow(() -> new StudentException(StudentErrorCode.STUDENT_NOT_FOUND));

		if (!passwordEncoder.matches(signInReq.password(), student.getPassword())) {
			throw new AuthException(AuthErrorCode.PASSWORD_MISMATCH);
		}

		String token = tokenGenerator.generateToken(student.getId(), student.getIdentifier());
		return SignInRes.of(student.getId(), token);
	}
}
