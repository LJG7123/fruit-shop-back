package com.web.jwt;

import com.web.domain.Member;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

	private final SecretKey secretKey;

	public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
		secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	public String getUsername(String token) {
		String re = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
		log.info("getUsername(String token)  re = {}", re);
		return re;
	}

	public String getId(String token) {
		String re = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", String.class);
		log.info("getIds(String token)  re = {}", re);
		return re;
	}

	public String getRole(String token) {
		String re = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
		log.info("getRole(String token)  re = {} ", re);
		return re;
	}

	public String getTokenType(String token) {
		String re = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
		log.info("getTokenType(String token)  re = {} ", re);
		return re;
	}

	public String createJwt(Member member, String role, Long expiredMs, TokenType type) {
		return Jwts.builder()
				.claim("username", member.getName())
				.claim("id", member.getId())
				.claim("role", role)
				.claim("category", type.getName())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expiredMs))
				.signWith(secretKey)
				.compact();
	}
}
