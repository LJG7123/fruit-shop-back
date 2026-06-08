package com.web.util;

import com.web.exception.CustomException;
import com.web.exception.ErrorCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor
public class PageValidator {

	public static void validatePageRange(Page<?> page, Pageable pageable) {
		if (page.getTotalPages() == 0) return;

		if (pageable.getPageNumber() >= page.getTotalPages()) {
			throw new CustomException(ErrorCode.PAGE_OUT_OF_RANGE);
		}
	}
}
