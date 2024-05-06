package com.spring.electronicshop.controller.api.admin.staff;

import com.spring.electronicshop.dto.request.PageDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FormSearchStaff extends PageDTO {
	private String name;
	private String email;

}
