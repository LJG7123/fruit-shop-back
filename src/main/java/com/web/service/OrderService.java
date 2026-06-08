package com.web.service;

import com.web.domain.Member;
import com.web.domain.Order;
import com.web.dto.OrderRequest;

import java.util.List;

public interface OrderService {

	Order placeOrder(OrderRequest orderRequest, Member member);

	List<Order> findAll();

	Order findById(Long id, Member member);

	List<Order> findMemberOrders(Member member);
}
