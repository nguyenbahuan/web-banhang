package com.spring.electronicshop.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class UserDTO {
	private long id;

	private String name;

	private String email;

	private String password;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;

	private RoleDTO role;

	private String phoneNumber;
	private String avatar;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedDate;

	private boolean isActive;

	@Transient
	private int age;

	public int getAge() {
		return Period.between(dob, LocalDate.now()).getYears();
	}

	public void setAge(int age) {
		this.age = age;
	}

}
