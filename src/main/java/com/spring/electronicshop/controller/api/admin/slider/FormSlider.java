package com.spring.electronicshop.controller.api.admin.slider;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormSlider {
	@NotNull(message = "{slider.image.notnull}")
	private List<MultipartFile> images;
	@NotNull(message = "{slider.content.notnull}")
	@NotEmpty(message = "{slider.content.notnull}")
	private String content;
}
