package com.web.dto;

import com.web.domain.Member;
import org.springframework.web.util.HtmlUtils;

public record SignUpResponse(
		Long memberNo,
		String id,
		String pwd,
		String name,
		String address,
		String role
) {
	public static SignUpResponse from(Member member) {
		return new SignUpResponse(
				member.getMemberNo(),
				HtmlUtils.htmlEscape(member.getId()),
				HtmlUtils.htmlEscape(member.getPwd()),
				HtmlUtils.htmlEscape(member.getName()),
				HtmlUtils.htmlEscape(member.getAddress()),
				HtmlUtils.htmlEscape(member.getRole())
		);
	}
}
