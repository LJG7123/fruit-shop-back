package com.web;

import com.web.domain.Member;
import com.web.domain.Order;
import com.web.domain.OrderLine;
import com.web.domain.Product;
import com.web.repository.MemberRepository;
import com.web.repository.OrderRepository;
import com.web.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class FruitShopBackApplicationTests {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("관리자_계정_등록")
	void test1() {
		if (!memberRepository.existsById("admin")) {
			String encPwd = passwordEncoder.encode("1234");

			memberRepository.save(Member.builder().id("admin").pwd(encPwd)
					.role("ROLE_ADMIN").address("-").name("관리자").build());
		}
	}

	@Test
	@DisplayName("사용자_계정_등록")
	void test2() {
		if (!memberRepository.existsById("test")) {
			String encPwd = passwordEncoder.encode("1234");

			memberRepository.save(Member.builder().id("test").pwd(encPwd)
					.role("ROLE_USER").address("경기도 성남시").name("test").build());
		}
	}

	@Test
	@DisplayName("상품_등록")
	void test3() {
		for (int i = 1; i <= 100; i++) {
			Product p = Product.builder().title("product " + i).content("content " + i).imgUrl("-").price(i * 10L).build();
			productRepository.save(p);
		}
	}

	@Test
	@DisplayName("상품_주문")
	void test4() {
		Member m = memberRepository.findById("test");
		Product p = productRepository.findById(1L).orElse(null);
		Order order = Order.builder().address("test").member(m).build();
		List<OrderLine> lines = List.of(
				OrderLine.builder().order(order).product(p).quantity(2).build()
		);
		order.setOrderLines(lines);
		System.out.println(order);

		orderRepository.save(order);
	}

}
