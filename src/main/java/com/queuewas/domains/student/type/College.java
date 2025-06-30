package com.queuewas.domains.student.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum College {

	DIGITAL_CONVERGENCE("디지털융합대학"),
	ENGINEERING("공과대학");

	private final String name;

}
