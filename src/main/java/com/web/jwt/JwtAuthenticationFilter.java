package com.web.jwt;

import com.web.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		String method = request.getMethod();

		return path.equals("/login")
				|| path.equals("/signup")
				|| path.equals("/reissue")
				|| path.equals("/error")
				|| path.startsWith("/swagger-ui")
				|| path.startsWith("/v3/api-docs")
				|| ("GET".equals(method) && path.startsWith("/product"));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");

		if (authorization == null) {
			doFilter(request, response, filterChain);
			return;
		}

		if (!authorization.startsWith("Bearer ")) {
			writeErrorResponse(response, ErrorCode.INVALID_ACCESS_TOKEN);
			return;
		}

		try {
			String token = authorization.substring(7);

			if (!StringUtils.hasText(token) || !TokenType.ACCESS.getName().equals(jwtUtil.getTokenType(token))) {
				writeErrorResponse(response, ErrorCode.INVALID_ACCESS_TOKEN);
				return;
			}

			String id = jwtUtil.getId(token);
			String role = jwtUtil.getRole(token);

			User userDetails = new User(id, "", java.util.List.of(new SimpleGrantedAuthority(role)));

			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			writeErrorResponse(response, ErrorCode.ACCESS_TOKEN_EXPIRED);
		} catch (JwtException | IllegalArgumentException e) {
			writeErrorResponse(response, ErrorCode.INVALID_ACCESS_TOKEN);
		}
	}

	private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		response.setStatus(errorCode.getStatus().value());
		response.setContentType("application/problem+json");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		ProblemDetail problemDetail = ProblemDetail.forStatus(errorCode.getStatus());
		problemDetail.setTitle(errorCode.getTitle());
		problemDetail.setDetail(errorCode.getMessage());
		problemDetail.setProperty("code", errorCode.name());

		response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
	}
}
