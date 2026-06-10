package com.web.controller;

import com.web.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Token API")
public class AuthController {

	private final RefreshTokenService refreshTokenService;

	@PostMapping("/reissue")
	public ResponseEntity<?> reissue(@RequestHeader("Refresh-Token") String refreshToken, HttpServletResponse response) {
		String accessToken = refreshTokenService.reissue(refreshToken);
		response.addHeader("Authorization", "Bearer " + accessToken);
		return ResponseEntity.ok().build();
	}
}
