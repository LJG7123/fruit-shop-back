package com.web.service;

import com.web.domain.Member;
import com.web.dto.SignUpRequest;

public interface MemberService {

	boolean isDuplicateId(String id);

	Member signUp(SignUpRequest member);
}
