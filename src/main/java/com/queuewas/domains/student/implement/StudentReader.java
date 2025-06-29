package com.queuewas.domains.student.implement;

import java.util.Optional;

import com.queuewas.common.annotation.Implementation;
import com.queuewas.domains.student.domain.Student;
import com.queuewas.domains.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class StudentReader {

	private final StudentRepository studentRepository;

	public Optional<Student> readByIdentifier(String identifier) {
		return studentRepository.findByIdentifier(identifier);
	}
}
