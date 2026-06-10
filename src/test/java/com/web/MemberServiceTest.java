package com.web;

import com.web.dto.SignUpRequest;
import com.web.exception.CustomException;
import com.web.exception.ErrorCode;
import com.web.repository.MemberRepository;
import com.web.service.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	MemberRepository memberRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	MemberServiceImpl memberService;

	@Test
	@DisplayName("signUp throws DUPLICATE_ID when id already exists")
	void signUp_duplicateId_throwsException() {
		SignUpRequest request = new SignUpRequest("test", "1234", "test user", "Seoul");
		when(memberRepository.existsById(request.id())).thenReturn(true);

		CustomException exception = assertThrows(CustomException.class, () -> memberService.signUp(request));

		assertEquals(ErrorCode.DUPLICATE_ID, exception.getErrorCode());
		verify(memberRepository).existsById(request.id());
		verify(passwordEncoder, never()).encode(request.pwd());
		verify(memberRepository, never()).save(any());
	}
}
