package com.web.service;

public interface RefreshTokenService {

	void saveOrUpdate(String memberId, String refreshToken);

	String reissue(String refreshToken);
}
