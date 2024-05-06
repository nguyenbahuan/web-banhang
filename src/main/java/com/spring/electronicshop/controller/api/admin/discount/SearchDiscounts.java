package com.spring.electronicshop.controller.api.admin.discount;

import com.spring.electronicshop.dto.request.PageDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SearchDiscounts extends PageDTO {

	private double discountPercentage;
	private String discountName;
}
