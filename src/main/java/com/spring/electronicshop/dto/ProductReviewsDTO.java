package com.spring.electronicshop.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ProductReviewsDTO {
	
	private ProductsDTO products;
	private UserDTO user;
	private int rating;
	private String comment;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedDate;

	private boolean isActive;
}
