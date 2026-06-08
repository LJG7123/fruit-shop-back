package com.web.controller;

import com.web.domain.Member;
import com.web.domain.Order;
import com.web.dto.OrderRequest;
import com.web.dto.OrderResponse;
import com.web.security.MemberDetails;
import com.web.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@Tag(name = "Order API")
public class OrderController {

	@Value("${PAGE_COUNT}")
	private int PAGE_SIZE;

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
	public ResponseEntity<List<OrderResponse>> findAllOrders(@RequestParam(defaultValue = "1") int page) {
		Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
		Page<Order> orders = orderService.findAll(pageable);

		return new ResponseEntity<>(orders.stream().map(OrderResponse::from).toList(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> findOrderById(@PathVariable Long id) {
		Order order = orderService.findById(id, getCurrentMember());

		return new ResponseEntity<>(OrderResponse.from(order), HttpStatus.OK);
	}

	@GetMapping("/me")
	public ResponseEntity<List<OrderResponse>> findAllOrdersByMember(@RequestParam(defaultValue = "1") int page) {
		Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
		Page<Order> orders = orderService.findByMember(getCurrentMember(), pageable);

		return new ResponseEntity<>(orders.stream().map(OrderResponse::from).toList(), HttpStatus.OK);
	}
}
