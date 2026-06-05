package com.web.service;

import com.web.domain.Member;

public interface MemberService {

	boolean isDuplicateId(String id);

	Member signUp(Member member);
}
