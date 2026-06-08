package com.web.service;

import com.web.domain.Member;
import com.web.domain.Order;
import com.web.dto.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

	Order placeOrder(OrderRequest orderRequest, Member member);

	Page<Order> findAll(Pageable pageable);

	Order findById(Long id, Member member);

	Page<Order> findByMember(Member member, Pageable pageable);
}
