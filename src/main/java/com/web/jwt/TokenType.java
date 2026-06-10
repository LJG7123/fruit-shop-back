package com.web.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenType {

	ACCESS("access"),
	REFRESH("refresh");

	private final String name;
}
