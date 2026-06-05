package com.web.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberNo;

	@Column(unique = true)
	private String id;
	private String pwd;
	private String name;
	private String address;
	private String role;

	@CreationTimestamp
	private LocalDateTime createdAt;
}
