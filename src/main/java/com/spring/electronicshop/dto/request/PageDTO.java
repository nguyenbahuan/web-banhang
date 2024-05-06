package com.spring.electronicshop.dto.request;

import lombok.Data;

@Data
public class PageDTO {
	private Integer pageSize = 10;
	private Integer page = 0;

	public Integer getOffset() {
		return (page - 1) * pageSize;
	}

}
