package com.spring.electronicshop.controller.api.admin.discount;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormDiscount {
	
	@NotNull(message = "{discountPercentage.notnul}")
	@DecimalMax(value =  "0.99", message = "{discount.max}")
	@DecimalMin(value =  "0.01", message = "{discount.min}") 
	private Double discountPercentage;
	
	@NotEmpty(message = "{discount.notnull}")
	@NotNull(message = "{discount.notnull}")
	private String discountName;
	
}
