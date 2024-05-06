package com.spring.electronicshop.controller.api.user.oder;

import java.time.LocalDateTime;
import java.util.List;

import com.spring.electronicshop.dto.OderDetailsDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FormOder {
	@NotEmpty(message = "{order.method.notnull}")
	@NotNull(message = "{order.method.notnull}")
	private String payMethod;
	private int status = 0;
	private LocalDateTime createdDate = LocalDateTime.now();
	private Long userId;
	@NotNull(message = "{order.address.notnull}")
	private Long addressId;
	@NotEmpty(message = "{order.orderdetai}")
	@NotNull(message = "{order.orderdetai}")
	List<OderDetailsDTO> oderDetails;

}
