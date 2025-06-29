package com.queuewas.domains.student.domain;


import com.queuewas.domains.student.type.Semester;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentPolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_policy_id")
	private Long id;

	private int maxCredits;
	private int minCredits;
	private int currentCredits;

	@Enumerated(EnumType.STRING)
	private Semester semester;

	@OneToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@Builder
	public StudentPolicy(int maxCredits, int minCredits, int currentCredits, Semester semester, Student student) {
		this.maxCredits = maxCredits;
		this.minCredits = minCredits;
		this.currentCredits = currentCredits;
		this.semester = semester;
		this.student = student;
	}

	public static StudentPolicy of(Student student) {
		return StudentPolicy.builder()
			.maxCredits(18)
			.minCredits(15)
			.currentCredits(0)
			.semester(Semester.FIRST)
			.student(student)
			.build();
	}

	public void increaseCurrentCredits(int credits) {
		this.currentCredits += credits;
	}

	public void decreaseCurrentCredits(int credits) {
		this.currentCredits -= credits;
	}
}
