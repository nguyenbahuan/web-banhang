package com.spring.electronicshop.controller.api.admin.category;

import com.spring.electronicshop.dto.request.PageDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FormSearchCategory extends PageDTO {
	private String name;
}
