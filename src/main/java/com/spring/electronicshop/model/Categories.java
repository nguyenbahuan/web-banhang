package com.spring.electronicshop.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "categories")
public class Categories {

	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String name;

//

	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	private boolean isActive;
	@OneToMany(mappedBy = "categories", cascade = CascadeType.ALL)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<Products> products;

	public Categories() {

	}

	public Categories(String name, LocalDateTime createdDate, boolean isActive) {

		this.name = name;
		this.createdDate = createdDate;
		this.isActive = isActive;
	}

}
