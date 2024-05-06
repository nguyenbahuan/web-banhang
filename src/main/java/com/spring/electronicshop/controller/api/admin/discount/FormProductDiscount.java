package com.spring.electronicshop.controller.api.admin.discount;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormProductDiscount {
	@NotNull(message = "Thiếu sản phẩm")
	private Long productId;
	@NotNull(message = "Thiếu discount")
	private Long discountId;
	@NotNull(message = "Thiếu ngày bắt đầu")
	private LocalDate startDate;
	@NotNull(message = "Thiếu ngày kết thúc")
	private LocalDate endDate;
}
