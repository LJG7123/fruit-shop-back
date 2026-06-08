package com.web.dto;

import java.util.List;

public record OrderRequest(
		String address,
		List<OrderItemRequest> items
) {
	public record OrderItemRequest(
			Long productId,
			Integer quantity
	) {}
}
