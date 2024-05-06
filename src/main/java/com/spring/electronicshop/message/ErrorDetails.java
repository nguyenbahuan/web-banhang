package com.spring.electronicshop.message;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
	private Date timestamp;
	private String message;
	private String details;
}
