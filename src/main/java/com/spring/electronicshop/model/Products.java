package com.spring.electronicshop.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Products {
	@Id
	@GeneratedValue
	private long id;

	@Column
	private String name;

	@Column
	private int price;

	@Column
	private String description;
	@Column
	private int totalQuantity;

	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private boolean isActive;

	@ManyToOne()
	@JoinColumn(name = "category_id", nullable = false)
	private Categories categories;

	@OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
	private List<Images> images;

	@OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
	private List<ProductReviews> productReviews;

	@OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
	private List<OdersDetails> odersDetails;

	public Products() {

	}

	public Products(String name, String description, int price, Categories categories, int totalQuantity,
			LocalDateTime createdDate, boolean isActive) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.categories = categories;
		this.totalQuantity = totalQuantity;
		this.createdDate = createdDate;
		this.isActive = isActive;
	}

	

}
