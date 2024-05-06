package com.spring.electronicshop.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "address")
public class Address {
	@Id
	@GeneratedValue
	private Long id;

	private String address;

	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private boolean isActive;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
