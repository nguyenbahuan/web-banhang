package com.spring.electronicshop.controller.api.user.reviews;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormReviews {
	
	private Long productId;

	@NotNull(message = "{rating.rating}")
	private Integer rating;
	@NotEmpty(message = "{rating.comment}")
	@NotNull(message = "{rating.comment}")
	private String comment;

}
