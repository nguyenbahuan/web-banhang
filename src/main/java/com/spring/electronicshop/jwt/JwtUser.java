package com.spring.electronicshop.jwt;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtUser {

	private Long id;
	private String name;
	private String avatar;
	private String email;
	private String phoneNumber;
	private String tokenAccess;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate dob;
	private String role;

	private int statusCode;
	private String error;
	private String message;

}
