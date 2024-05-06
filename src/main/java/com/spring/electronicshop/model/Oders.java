package com.spring.electronicshop.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
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
@Table(name = "oders")
public class Oders {
	@Id
	@GeneratedValue
	private Long id;

	private String payMethod;

	private Long total;

	private int status;

	private LocalDateTime createdDate;

	@OneToMany(mappedBy = "oders", cascade = CascadeType.ALL)
	private List<OdersDetails> odersDetails;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "address_id", nullable = false)
	private Address address;

}
