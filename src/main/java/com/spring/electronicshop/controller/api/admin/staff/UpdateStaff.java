package com.spring.electronicshop.controller.api.admin.staff;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStaff {

	private Long roleId;

	@NotNull(message = "thiếu tên")
	private String name;

	@NotEmpty(message = "thiếu số điện thoại")
	@NotNull(message = "thiếu số điện thoại")
	@Length(max = 10, min = 10, message = "Số điện thoại phải có 10 số")
	private String phoneNumber;

	@NotNull(message = "thiếu ngày sinh")
	private LocalDate dob;

	private List<MultipartFile> avatar;

	private Boolean active;
}
