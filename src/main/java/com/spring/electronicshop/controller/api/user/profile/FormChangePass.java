package com.spring.electronicshop.controller.api.user.profile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormChangePass {

	private Long userId;
	@NotNull(message = "{oldpassword.notnull}")
	@NotEmpty(message = "{oldpassword.notnull}")
	private String oldPassword;
	@NotNull(message = "{password.notnull}")
	@NotEmpty(message = "{password.notnull}")
	private String newPassword;
	@NotNull(message = "{passwordconfirm.notnull}")
	@NotEmpty(message = "{passwordconfirm.notnull}")
	private String confirmPassword;
}
