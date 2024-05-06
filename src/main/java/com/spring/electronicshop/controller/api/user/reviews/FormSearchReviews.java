package com.spring.electronicshop.controller.api.user.reviews;

import com.spring.electronicshop.dto.request.PageDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FormSearchReviews extends PageDTO {
	private Long productId;
	private Long userId;
}
