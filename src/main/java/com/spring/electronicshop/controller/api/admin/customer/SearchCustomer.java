package com.spring.electronicshop.controller.api.admin.customer;

import com.spring.electronicshop.dto.request.PageDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchCustomer extends PageDTO {
	private String name;
}
