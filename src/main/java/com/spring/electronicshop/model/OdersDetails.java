package com.spring.electronicshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "oders_details")
public class OdersDetails {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Products products;

	@ManyToOne
	@JoinColumn(name = "oder_id", nullable = false)
	private Oders oders;

	private int quantity;
	
	private Long subPrice;

}
