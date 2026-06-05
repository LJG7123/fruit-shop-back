package com.web.dto;

import com.web.domain.Product;

public record ProductRequest(
		String title,
		String content,
		String imgUrl,
		Long price
) {
	public Product toProduct() {
		return Product.builder().title(title).content(content).imgUrl(imgUrl).price(price).build();
	}
}
