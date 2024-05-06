package com.spring.electronicshop.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product_discount")
public class ProductDiscount {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne()
	@JoinColumn(name = "product_id", nullable = false)
	private Products products;

	@ManyToOne()
	@JoinColumn(name = "discount_id", nullable = false)
	private Discounts discounts;

	private LocalDate startDate;
	private LocalDate endDate;
}
