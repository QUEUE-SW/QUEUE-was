package com.queuewas.domains.student.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Major {

	CSE("컴퓨터공학전공"),
	ICE("정보통신공학전공"),
	SC("소프트웨어융합전공"),
	ALL("컴퓨터학부");

	private final String name;
}
