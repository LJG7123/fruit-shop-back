package com.web.service;

import com.web.domain.Member;
import com.web.dto.SignUpRequest;
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
	public Member signUp(SignUpRequest member) {
		if (isDuplicateId(member.id())) {
			throw new IllegalArgumentException("Duplicate member id: " + member.id());
		}

		Member m = member.toMember();
		m.setPwd(passwordEncoder.encode(m.getPwd()));
		if (m.getRole() == null || m.getRole().isBlank()) {
			m.setRole("ROLE_USER");
		}

		return memberRepository.save(m);
	}
}
