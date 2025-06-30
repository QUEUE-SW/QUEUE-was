package com.queuewas.domains.student.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Department {

	COMPUTER_SCIENCE_AND_ENGINEERING("컴퓨터학부"),
	ELECTRONIC_ENGINEERING("전자공학과"),
	ROBOTICS_ENGINEERING("로봇공학과");

	private final String name;
}
