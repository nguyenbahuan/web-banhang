package com.spring.electronicshop.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CategoriesDTO {
	private Long id;
	private String name;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime createdDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime updatedDate;

	private boolean isActive;
}
