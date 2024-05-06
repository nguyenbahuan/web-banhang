package com.spring.electronicshop.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ProductsDTO {
	private Long id;
	private String name;
	private Long price;
	private Long priceDiscount;
	private CategoriesDTO category;
	private Integer discountPercentage;

	private String description;

	private int totalQuantity;

	private List<ImagesDTO> images;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedDate;
	
	private Double rating;

	private boolean isActive;

}
