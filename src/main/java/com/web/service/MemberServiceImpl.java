package com.web.service;

import com.web.domain.Member;
import com.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public boolean isDuplicateId(String id) {
		return memberRepository.existsById(id);
	}

	@Override
	public Member signUp(Member member) {
		if (isDuplicateId(member.getId())) {
			throw new IllegalArgumentException("Duplicate member id: " + member.getId());
		}

		member.setPwd(passwordEncoder.encode(member.getPwd()));
		if (member.getRole() == null || member.getRole().isBlank()) {
			member.setRole("ROLE_USER");
		}

		return memberRepository.save(member);
	}
}
