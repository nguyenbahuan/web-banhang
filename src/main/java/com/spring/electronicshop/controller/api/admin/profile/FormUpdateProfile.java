package com.spring.electronicshop.controller.api.admin.profile;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormUpdateProfile {
	@NotEmpty(message = "thiếu trường tên")
	@NotNull(message = "thiếu trường tên")
	private String name;
	
	private List<MultipartFile> avatar;
}
