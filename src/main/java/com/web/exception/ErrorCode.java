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
	PERMISSION_DENIED(HttpStatus.FORBIDDEN, "Permission Denied", "접근 권한이 없습니다.");

	private final HttpStatus status;
	private final String title;
	private final String message;
}
