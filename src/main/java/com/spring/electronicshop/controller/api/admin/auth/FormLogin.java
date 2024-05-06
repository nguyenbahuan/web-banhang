package com.spring.electronicshop.controller.api.admin.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormLogin {
	@Email(message = "{email.valid}")
	@NotEmpty(message = "{email.notnull}")
	@NotNull(message = "{email.notemty}")
	private String email;
	@NotEmpty(message = "{password.notnull}")
	@NotNull(message = "{password.notemty}")
	private String password;

}
