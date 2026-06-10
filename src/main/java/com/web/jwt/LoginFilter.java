package com.web.jwt;

import com.web.domain.Member;
import com.web.repository.MemberRepository;
import com.web.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collection;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;
	private final RefreshTokenService refreshTokenService;

	public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, MemberRepository memberRepository
			, RefreshTokenService refreshTokenService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.memberRepository = memberRepository;
		this.refreshTokenService = refreshTokenService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String id = obtainUsername(request);
		String pwd = obtainPassword(request);

		if (!StringUtils.hasText(id) || !StringUtils.hasText(pwd)) {
			throw new AuthenticationServiceException("id and pwd are required");
		}

		UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(id, pwd);
		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        FilterChain chain,
	                                        Authentication authentication) throws IOException {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Member member = memberRepository.findById(userDetails.getUsername());

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		GrantedAuthority auth = authorities.iterator().next();
		String role = auth.getAuthority(); //ROLE_USER or ROLE_ADMIN

		String accessToken = jwtUtil.createJwt(member, role, 1000L * 60 * 30, TokenType.ACCESS);
		String refreshToken = jwtUtil.createJwt(member, role, 1000L * 60 * 60 * 24 * 7, TokenType.REFRESH);

		refreshTokenService.saveOrUpdate(member.getId(), refreshToken);

		response.addHeader("Authorization", "Bearer " + accessToken);
		response.addHeader("Refresh-Token", refreshToken);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
	                                          AuthenticationException failed) {
		log.error("Authentication failed: {}", failed.getMessage());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
