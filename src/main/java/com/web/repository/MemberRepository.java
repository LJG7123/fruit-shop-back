package com.web.repository;

import com.web.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Boolean existsById(String id);

	Member findById(String id);
}
