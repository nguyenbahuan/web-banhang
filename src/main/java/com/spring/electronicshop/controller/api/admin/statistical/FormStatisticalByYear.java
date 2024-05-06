package com.spring.electronicshop.controller.api.admin.statistical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class FormStatisticalByYear {
	private Integer month;
	private Integer year;
	private Long total;

}
