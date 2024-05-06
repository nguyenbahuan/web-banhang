package com.spring.electronicshop.controller.api.admin.product;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormUpdateProduct {
	private Long id;
	@NotBlank(message = "required name product")
	@NotEmpty(message = "required name product")
	private String name;

	@NotNull(message = "required price")
	@Min(value = 1000, message = "price is so short")
	@Max(value = 1999999999, message = "price is so hiegt")
	private Integer price;
	@NotBlank(message = "required description")
	private String description;

	@Min(value = 10, message = "total is so short")
	@NotNull(message = "required totalQuantity")
	@Max(value = 999999, message = "price is so hiegt")
	private Integer totalQuantity = null;

	private Long categoryId;

	
	private List<MultipartFile> images;
}
