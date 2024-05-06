package com.spring.electronicshop.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.electronicshop.model.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {
	private int statusCode;
	private String error;
	private String message;
	private String token;
	private String refreshToken;
	private String expirationTime;
	@NotBlank(message = "required name")
	@NotNull(message = "required name")
	private String name;
	@NotBlank(message = "required email")
	@NotNull(message = "required email")
	private String email;
//	@NotBlank(message = "required date of birth")
	@NotNull(message = "required date of birth")
//	@NotEmpty(message = "required date of birth")
	private LocalDate dob;
	@NotBlank(message = "required role")
	@NotNull(message = "required role")
	private String role;
	@NotBlank(message = "required password")
	@NotNull(message = "required password")
	private String password;
	private User user;

}
