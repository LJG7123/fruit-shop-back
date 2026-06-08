package com.web.service;

import com.web.domain.Member;
import com.web.domain.Order;
import com.web.dto.OrderRequest;

public interface OrderService {

	Order placeOrder(OrderRequest orderRequest, Member member);
}
