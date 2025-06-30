package com.queuewas.domains.student.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.queuewas.domains.student.domain.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	Optional<Student> findByIdentifier(String identifier);
}
