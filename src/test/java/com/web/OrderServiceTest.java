package com.web;

import com.web.domain.Member;
import com.web.domain.Order;
import com.web.domain.OrderLine;
import com.web.domain.Product;
import com.web.dto.OrderRequest;
import com.web.exception.CustomException;
import com.web.exception.ErrorCode;
import com.web.repository.OrderRepository;
import com.web.repository.ProductRepository;
import com.web.service.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	OrderRepository orderRepository;

	@Mock
	ProductRepository productRepository;

	@InjectMocks
	OrderServiceImpl orderService;

	@Test
	@DisplayName("placeOrder creates order lines and saves order")
	void placeOrder_createsOrderLinesAndSavesOrder() {
		Member member = member(1L, "user", "ROLE_USER");
		Product apple = product(1L, "Apple");
		Product banana = product(2L, "Banana");
		OrderRequest request = new OrderRequest("Seoul", List.of(
				new OrderRequest.OrderItemRequest(1L, 2),
				new OrderRequest.OrderItemRequest(2L, 3)
		));
		when(productRepository.findById(1L)).thenReturn(Optional.of(apple));
		when(productRepository.findById(2L)).thenReturn(Optional.of(banana));
		when(orderRepository.save(org.mockito.ArgumentMatchers.any(Order.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		Order result = orderService.placeOrder(request, member);

		assertEquals("Seoul", result.getAddress());
		assertSame(member, result.getMember());
		assertEquals(2, result.getOrderLines().size());
		assertOrderLine(result.getOrderLines().get(0), result, apple, 2);
		assertOrderLine(result.getOrderLines().get(1), result, banana, 3);
		verify(productRepository).findById(1L);
		verify(productRepository).findById(2L);
		verify(orderRepository).save(result);
	}

	@Test
	@DisplayName("placeOrder throws PRODUCT_NOT_FOUND when product does not exist")
	void placeOrder_missingProduct_throwsException() {
		Member member = member(1L, "user", "ROLE_USER");
		OrderRequest request = new OrderRequest("Seoul", List.of(
				new OrderRequest.OrderItemRequest(1L, 2)
		));
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		CustomException exception = assertThrows(CustomException.class, () -> orderService.placeOrder(request, member));

		assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
		verify(productRepository).findById(1L);
	}

	@Test
	@DisplayName("findAll returns paged orders")
	void findAll_returnsPagedOrders() {
		Pageable pageable = PageRequest.of(0, 10);
		Order order = order(1L, member(1L, "user", "ROLE_USER"));
		Page<Order> page = new PageImpl<>(List.of(order), pageable, 1);
		when(orderRepository.findAll(pageable)).thenReturn(page);

		Page<Order> result = orderService.findAll(pageable);

		assertEquals(1, result.getTotalElements());
		assertSame(order, result.getContent().get(0));
		verify(orderRepository).findAll(pageable);
	}

	@Test
	@DisplayName("findById returns order for owner")
	void findById_owner_returnsOrder() {
		Member owner = member(1L, "user", "ROLE_USER");
		Order order = order(1L, owner);
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		Order result = orderService.findById(1L, owner);

		assertSame(order, result);
		verify(orderRepository).findById(1L);
	}

	@Test
	@DisplayName("findById returns order for admin")
	void findById_admin_returnsOrder() {
		Member owner = member(1L, "user", "ROLE_USER");
		Member admin = member(2L, "admin", "ROLE_ADMIN");
		Order order = order(1L, owner);
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		Order result = orderService.findById(1L, admin);

		assertSame(order, result);
		verify(orderRepository).findById(1L);
	}

	@Test
	@DisplayName("findById throws PERMISSION_DENIED for another user")
	void findById_anotherUser_throwsException() {
		Member owner = member(1L, "user", "ROLE_USER");
		Member anotherUser = member(2L, "other", "ROLE_USER");
		Order order = order(1L, owner);
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

		CustomException exception = assertThrows(CustomException.class, () -> orderService.findById(1L, anotherUser));

		assertEquals(ErrorCode.PERMISSION_DENIED, exception.getErrorCode());
		verify(orderRepository).findById(1L);
	}

	@Test
	@DisplayName("findById throws ORDER_NOT_FOUND when order does not exist")
	void findById_missingOrder_throwsException() {
		Member member = member(1L, "user", "ROLE_USER");
		when(orderRepository.findById(1L)).thenReturn(Optional.empty());

		CustomException exception = assertThrows(CustomException.class, () -> orderService.findById(1L, member));

		assertEquals(ErrorCode.ORDER_NOT_FOUND, exception.getErrorCode());
		verify(orderRepository).findById(1L);
	}

	@Test
	@DisplayName("findByMember returns member orders")
	void findByMember_returnsPagedOrders() {
		Member member = member(1L, "user", "ROLE_USER");
		Pageable pageable = PageRequest.of(0, 10);
		Order order = order(1L, member);
		Page<Order> page = new PageImpl<>(List.of(order), pageable, 1);
		when(orderRepository.findAllByMember(member, pageable)).thenReturn(page);

		Page<Order> result = orderService.findByMember(member, pageable);

		assertEquals(1, result.getTotalElements());
		assertSame(order, result.getContent().get(0));
		verify(orderRepository).findAllByMember(member, pageable);
	}

	private void assertOrderLine(OrderLine orderLine, Order order, Product product, int quantity) {
		assertSame(order, orderLine.getOrder());
		assertSame(product, orderLine.getProduct());
		assertEquals(quantity, orderLine.getQuantity());
	}

	private Member member(Long memberNo, String id, String role) {
		return Member.builder()
				.memberNo(memberNo)
				.id(id)
				.name(id)
				.role(role)
				.build();
	}

	private Product product(Long id, String title) {
		return Product.builder()
				.id(id)
				.title(title)
				.content(title + " content")
				.imgUrl(title + ".jpg")
				.price(1000L)
				.build();
	}

	private Order order(Long orderId, Member member) {
		return Order.builder()
				.orderId(orderId)
				.address("Seoul")
				.member(member)
				.build();
	}
}
