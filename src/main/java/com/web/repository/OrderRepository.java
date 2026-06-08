package com.web.repository;

import com.web.domain.Member;
import com.web.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findAllByMember(Member member);
}
