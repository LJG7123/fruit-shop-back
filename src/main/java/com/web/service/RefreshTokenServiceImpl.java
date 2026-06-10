package com.web.service;

import com.web.domain.Member;
import com.web.domain.RefreshToken;
import com.web.exception.CustomException;
import com.web.exception.ErrorCode;
import com.web.jwt.JwtUtil;
import com.web.jwt.TokenType;
import com.web.repository.MemberRepository;
import com.web.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtUtil jwtUtil;

	@Override
	public void saveOrUpdate(String memberId, String refreshToken) {
		RefreshToken token = refreshTokenRepository.findByMemberId(memberId).orElse(null);

		if (token != null) {
			token.setToken(refreshToken);
			return;
		}

		token = RefreshToken.builder().memberId(memberId).token(refreshToken).build();
		refreshTokenRepository.save(token);
	}

	@Override
	public String reissue(String refreshToken) {
		try {
			if (!TokenType.REFRESH.getName().equals(jwtUtil.getTokenType(refreshToken))) {
				throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
			}

			String id = jwtUtil.getId(refreshToken);
			String role = jwtUtil.getRole(refreshToken);

			RefreshToken storedToken = refreshTokenRepository.findByMemberId(id)
					.orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

			if (!refreshToken.equals(storedToken.getToken())) {
				throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
			}

			Member member = memberRepository.findById(id);

			return jwtUtil.createJwt(member, role, 1000L * 60 * 60, TokenType.ACCESS);
		} catch (ExpiredJwtException e) {
			throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
		} catch (JwtException | IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
		}
	}
}
