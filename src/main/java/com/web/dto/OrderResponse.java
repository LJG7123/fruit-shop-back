package com.web.dto;

import com.web.domain.Order;
import com.web.domain.OrderLine;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
		Long orderId,
		String address,
		LocalDateTime orderedAt,
		String memberId,
		List<OrderItemResponse> products
) {
	public record OrderItemResponse(
			Long productId,
			Integer quantity
	) {
		public static OrderItemResponse from(OrderLine orderLine){
			return new OrderItemResponse(
					orderLine.getProduct().getId(),
					orderLine.getQuantity()
			);
		}
	}

	public static OrderResponse from(Order order) {
		return new OrderResponse(
				order.getOrderId(),
				HtmlUtils.htmlEscape(order.getAddress()),
				order.getOrderedAt(),
				HtmlUtils.htmlEscape(order.getMember().getId()),
				order.getOrderLines().stream().map(OrderItemResponse::from).toList()
		);
	}
}
