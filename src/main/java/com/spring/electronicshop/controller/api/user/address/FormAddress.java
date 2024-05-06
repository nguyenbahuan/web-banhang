package com.spring.electronicshop.controller.api.user.address;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormAddress {
	private Long userId;

	@NotEmpty(message = "{address.notnull}")
	@NotNull(message = "{address.notnull}")
	private String address;

}
