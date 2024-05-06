package com.spring.electronicshop.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OdersDTO {

	private Long id;

	private String payMethod;

	private Long total;

	private int status;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;

	private List<OderDetailsDTO> oderDetails;

	private String address;

	private Long userId;

	private String username;
	
	private String phoneNumber;
}
