package com.spring.electronicshop.dto;

import lombok.Data;

@Data
public class StatisticalByYear {
	private int year;
	private int month;
	private int day;
	private Long total;
}
