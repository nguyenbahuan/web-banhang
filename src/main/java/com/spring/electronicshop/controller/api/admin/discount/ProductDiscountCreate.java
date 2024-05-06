package com.spring.electronicshop.controller.api.admin.discount;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDiscountCreate {
	@NotEmpty(message = "Thiếu sản phẩm")
	@NotNull(message = "Thiếu sản phẩm")
	private List<Long> productId;
	@NotNull(message = "Thiếu discount")
	private Long discountId;

	@NotNull(message = "Thiếu ngày bắt đầu")
	private LocalDate startDate;

	@NotNull(message = "Thiếu ngày kết thúc")
	private LocalDate endDate;
}
