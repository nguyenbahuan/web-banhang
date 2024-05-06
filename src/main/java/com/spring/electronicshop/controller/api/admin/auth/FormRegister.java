package com.spring.electronicshop.controller.api.admin.auth;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormRegister {
	@NotNull(message = "")
	@NotEmpty(message = "")
	private String name;
	@NotNull(message = "")
	@NotEmpty(message = "")
	private String email;
	@NotNull(message = "")
	@NotEmpty(message = "")
	private String password;
	@NotNull(message = "")
	@NotEmpty(message = "")
	private String confirmPassword;
	@NotNull(message = "")
	private LocalDate dob;
	@NotNull(message = "")
	@NotEmpty(message = "")
	private String phoneNumber;
	@NotNull(message = "")
	private Long roleId;

}
