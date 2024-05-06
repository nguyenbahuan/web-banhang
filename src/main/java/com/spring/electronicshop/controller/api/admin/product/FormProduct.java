package com.spring.electronicshop.controller.api.admin.product;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormProduct {
	private Long id;

	@NotBlank(message = "{product.name.notnull}")
	@NotEmpty(message = "{product.name.notnull}")
	private String name;

	@NotNull(message = "{product.price.notnull}")
	@Min(value = 1000, message = "{product.price.min}")
	@Max(value = 1999999999, message = "{product.price.max}")
	private Integer price ;
	@NotBlank(message = "{product.description.notnull}")
	@Size(min = 20, message = "{validation.name.size.too_short}")
	@Size(max = 500, message = "{validation.name.size.too_long}")
	private String description;

	@Min(value = 1, message = "{product.totalQuantity.min}")
	@NotNull(message = "{product.totalQuantity.notnull}")
	@Max(value = 999999, message = "{product.totalQuantity.max}")
	private Integer totalQuantity;

	private Long categoryId;

	@NotNull(message = "{product.images.notnull}")
	@NotEmpty(message = "{product.images.notnull}")
	private List<MultipartFile> images;

}
