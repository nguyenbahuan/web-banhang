package com.spring.electronicshop.controller.api.admin.oder;

import com.spring.electronicshop.dto.request.PageDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchOder extends PageDTO {
	private Long userId;
	private String userName;
}
