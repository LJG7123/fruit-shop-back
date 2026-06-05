package com.web.dto;


import com.web.domain.Member;

public record SignUpRequest(
		String id,
		String pwd,
		String name,
		String address
) {

	public Member toMember() {
		return Member.builder().id(id).pwd(pwd).name(name).address(address).build();
	}
}
