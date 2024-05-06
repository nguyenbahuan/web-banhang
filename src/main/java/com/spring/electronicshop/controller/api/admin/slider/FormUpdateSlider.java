package com.spring.electronicshop.controller.api.admin.slider;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormUpdateSlider {

	private List<MultipartFile> images;
	@NotNull(message = "Thiếu nội dung")
	@NotEmpty(message = "Thiếu nội dung")
	private String content;
}
