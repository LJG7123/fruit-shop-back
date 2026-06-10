package com.web.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	DUPLICATE_ID(HttpStatus.CONFLICT, "Duplicate ID", "이미 사용중인 아이디입니다."),
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product Not Found", "상품을 찾을 수 없습니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order Not Found", "주문을 찾을 수 없습니다."),
	PERMISSION_DENIED(HttpStatus.FORBIDDEN, "Permission Denied", "접근 권한이 없습니다."),
	PAGE_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "Page Out Of Range", "요청한 페이지가 마지막 페이지를 벗어났습니다."),
	ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Access Token Expired", "액세스 토큰이 만료되었습니다."),
	REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh Token Expired", "리프레시 토큰이 만료되었습니다."),
	INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Access Token", "유효하지 않은 액세스 토큰입니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Refresh Token", "유효하지 않은 리프레시 토큰입니다."),
	REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh Token Not Found", "리프레시 토큰을 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String title;
	private final String message;
}
