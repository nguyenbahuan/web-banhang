package com.spring.electronicshop.controller.api.user.reviews;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class StatisticalReviewProduct {
	private Integer rating;
	private Long quantity;
}
