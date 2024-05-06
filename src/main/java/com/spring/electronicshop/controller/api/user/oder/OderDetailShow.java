package com.spring.electronicshop.controller.api.user.oder;

import com.spring.electronicshop.dto.ProductsDTO;

import lombok.Data;

@Data
public class OderDetailShow {
	private ProductsDTO products;
	private int quantity;
	private long subPrice;
}
