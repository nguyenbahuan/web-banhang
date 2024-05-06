package com.spring.electronicshop.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Copyright 2019 {@author Loda} (https://loda.me). This project is licensed
 * under the MIT license.
 *
 * @since 5/1/2019 Github: https://github.com/loda-kun
 */
@Data
public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
