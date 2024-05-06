package com.spring.electronicshop.controller.api.admin.oder;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOder {
	@NotEmpty(message = "{order.status.notnull}")
	private int status;

	@NotEmpty(message = "{order.note.notnull}")
	@NotNull(message = "{order.note.notnull}")
	private String note;
}
