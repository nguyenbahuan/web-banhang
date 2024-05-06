package com.spring.electronicshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "slider")
public class Slider {
	@Id
	@GeneratedValue
	private Long id;

	private String image;

	private String content;
}
