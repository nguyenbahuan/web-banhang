package com.spring.electronicshop.controller.api.user.auth;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormUserRegister {

	@NotNull(message = "{name.notnull}")
	@NotEmpty(message = "{name.notnull}")
	private String name;
	@Email(message = "{email.valid}")
	@NotNull(message = "{email.notnull}")
	@NotEmpty(message = "{email.notnull}")
	private String email;
	@NotNull(message = "{password.notnull}")
	@NotEmpty(message = "{password.notnull}")
	private String password;
	@NotNull(message = "{passwordconfirm.notnull}")
	@NotEmpty(message = "{passwordconfirm.notnull}")
	private String confirmPassword;
	@NotNull(message = "{dob.notnull}")
	private LocalDate dob;
	@NotNull(message = "{phone.notnull}")
	@NotEmpty(message = "{phone.notnull}")
	private String phoneNumber;

	private Long role_id;

}
