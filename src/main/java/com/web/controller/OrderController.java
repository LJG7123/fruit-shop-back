package com.web.controller;

import com.web.domain.Member;
import com.web.domain.Order;
import com.web.dto.OrderRequest;
import com.web.dto.OrderResponse;
import com.web.security.MemberDetails;
import com.web.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {

	private final OrderService orderService;
	private final UserDetailsService userDetailsService;

	@PostMapping
	public ResponseEntity<OrderResponse> addOrder(@RequestBody OrderRequest orderRequest) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = ((MemberDetails) userDetailsService.loadUserByUsername(username)).member();
		Order order = orderService.placeOrder(orderRequest, member);

		return new ResponseEntity<>(OrderResponse.from(order), HttpStatus.CREATED);
	}
}
