package com.web.service;

import com.web.domain.Member;
import com.web.domain.Order;
import com.web.domain.OrderLine;
import com.web.domain.Product;
import com.web.dto.OrderRequest;
import com.web.repository.OrderRepository;
import com.web.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	@Transactional
	@Override
	public Order placeOrder(OrderRequest orderRequest, Member member) {
		Order order = Order.builder().address(orderRequest.address()).member(member).build();
		List<OrderLine> orderLines = orderRequest.items().stream().map(item -> {
					Product product = productRepository.findById(item.productId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));
					return OrderLine.builder().order(order).product(product).quantity(item.quantity()).build();
				}
		).toList();
		order.setOrderLines(orderLines);

		return orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Order findById(Long id, Member member) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));

		boolean isAdmin = "ROLE_ADMIN".equals(member.getRole());
		boolean isOwner = order.getMember().getMemberNo().equals(member.getMemberNo());

		if (!isAdmin && !isOwner) {
			throw new IllegalArgumentException("permission denied");
		}

		return order;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Order> findMemberOrders(Member member) {
		return orderRepository.findAllByMember(member);
	}
}
