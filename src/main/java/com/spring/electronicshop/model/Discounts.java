package com.spring.electronicshop.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "discounts")
public class Discounts {
	@Id
	@GeneratedValue
	private Long id;

	private double discountPercentage;

	private String discountName;

	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private boolean isActive;

}
