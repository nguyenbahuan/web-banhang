package com.spring.electronicshop.controller.password;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormResetPassword {

	@NotEmpty(message = "Vui lòng nhập password")
	@NotNull(message = "Vui lòng nhập password")
	private String password;

}
