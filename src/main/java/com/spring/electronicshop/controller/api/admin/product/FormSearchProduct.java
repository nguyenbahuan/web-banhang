package com.spring.electronicshop.controller.api.admin.product;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.electronicshop.dto.RangeDTO;
import com.spring.electronicshop.dto.request.PageDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormSearchProduct extends PageDTO {
	private String name;
	private Boolean isActive;
	private Long categoryId;
	private Integer minPrice;
	private Integer maxPrice;
	private String description;
	private String orderBy;
	private Boolean isBestSeller;
	private Boolean isDiscouting;
	private List<RangeDTO> rangePrices = new ArrayList<>();

}
