package com.spring.electronicshop.controller.api.admin.staff;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormStaff {
	private long id;

	@NotNull(message = "{role.notnull}")
	private Long roleId;
	
	@NotEmpty(message = "{name.notnull}")
	@NotNull(message = "{name.notnull}")
	private String name;
	
	@Email(message = "{email.valid}")
	@NotEmpty(message = "{email.notnull}")
	@NotNull(message = "{email.notnull}")
	private String email;
	
	@NotEmpty(message = "{password.notnull}")
	@NotNull(message = "{password.notnull}")
	private String password;

	@NotNull(message = "{dob.notnull}")
	private LocalDate dob;
	
	@NotEmpty(message = "{phone.notnull}")
	@NotNull(message = "{phone.notnull}")
	@Length(max = 10, min = 10, message = "{phone.size}")
	private String phoneNumber;
	
	@NotEmpty(message = "{avatar.notnull}")
	@NotNull(message = "{avatar.notnull}")
	private List<MultipartFile> avatar;

	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private boolean isActive;

}
