package com.spring.electronicshop.controller.api.admin.statistical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class FormStatisticalByCategory {
	private Long id;
	private String name;
	private Long total;
}
