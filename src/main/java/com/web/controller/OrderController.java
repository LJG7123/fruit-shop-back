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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderController {

	private final OrderService orderService;
	private final UserDetailsService userDetailsService;

	private Member getCurrentMember() {
		String username =  SecurityContextHolder.getContext().getAuthentication().getName();

		return ((MemberDetails) userDetailsService.loadUserByUsername(username)).member();
	}

	@PostMapping
	public ResponseEntity<OrderResponse> addOrder(@RequestBody OrderRequest orderRequest) {
		Order order = orderService.placeOrder(orderRequest, getCurrentMember());

		return new ResponseEntity<>(OrderResponse.from(order), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<OrderResponse>> findAllOrders() {
		List<Order> orders = orderService.findAll();

		return new ResponseEntity<>(orders.stream().map(OrderResponse::from).toList(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> findOrderById(@PathVariable Long id) {
		Order order = orderService.findById(id, getCurrentMember());

		return new ResponseEntity<>(OrderResponse.from(order), HttpStatus.OK);
	}

	@GetMapping("/me")
	public ResponseEntity<List<OrderResponse>> findAllOrdersByMember() {
		List<Order> orders = orderService.findMemberOrders(getCurrentMember());

		return new ResponseEntity<>(orders.stream().map(OrderResponse::from).toList(), HttpStatus.OK);
	}
}
