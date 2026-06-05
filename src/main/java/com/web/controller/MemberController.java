package com.web.controller;

import com.web.domain.Member;
import com.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	public ResponseEntity<Member> signUp(@RequestBody Member member) {
		return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signUp(member));
	}
}
