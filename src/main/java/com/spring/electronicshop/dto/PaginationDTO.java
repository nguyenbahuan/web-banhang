package com.spring.electronicshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDTO<T> {
	private Integer page;
	private Long total;
	private T content;
}
