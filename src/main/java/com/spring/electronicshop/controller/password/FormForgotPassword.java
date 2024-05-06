package com.spring.electronicshop.controller.password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormForgotPassword {


	@Email(message = "Email không hợp lệ")
	@NotEmpty(message = "Thiếu email")
	@NotNull(message = "Thiếu email")
	private String email;

}
