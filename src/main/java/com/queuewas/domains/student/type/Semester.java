package com.queuewas.domains.student.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Semester {
	FIRST("1학기"), SUMMER("여름학기"), SECOND("2학기"), WINTER("겨울학기");

	private final String name;
}
