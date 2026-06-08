package com.web.advice;

import com.web.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultExceptionAdvice {

	@ExceptionHandler({CustomException.class})
	public ResponseEntity<?> handleException(CustomException e) {
		HttpStatus status = e.getErrorCode().getStatus();
		ProblemDetail problemDetail = ProblemDetail.forStatus(status);

		problemDetail.setTitle(e.getErrorCode().getTitle());
		problemDetail.setDetail(e.getErrorCode().getMessage());

		return new ResponseEntity<>(problemDetail, status);
	}
}
