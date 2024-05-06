package com.spring.electronicshop.controller.api.admin.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FormCategory {

	@NotBlank(message = "{category.notemty}")
	private String name;

}
