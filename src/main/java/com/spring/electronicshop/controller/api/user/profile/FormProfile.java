package com.spring.electronicshop.controller.api.user.profile;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormProfile {

	@NotEmpty(message = "Thiếu tên")
	@NotNull(message = "Thiếu tên")
	private String name;
	@NotEmpty(message = "Thiếu số điện thoại")
	@NotNull(message = "Thiếu số điện thoại")
	private String phoneNumber;

	private List<MultipartFile> avatar;

}
