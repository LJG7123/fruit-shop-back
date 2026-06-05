package com.web.dto;

import com.web.domain.Product;
import org.springframework.web.util.HtmlUtils;

public record ProductResponse(
		Long id,
		String title,
		String content,
		String imgUrl,
		Long price
) {
	public static ProductResponse from(Product product) {
		return new ProductResponse(
				product.getId(),
				HtmlUtils.htmlEscape(product.getTitle()),
				HtmlUtils.htmlEscape(product.getContent()),
				HtmlUtils.htmlEscape(product.getImgUrl()),
				product.getPrice()
		);
	}
}
