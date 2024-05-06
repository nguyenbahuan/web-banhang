package com.spring.electronicshop.payload;

import lombok.Data;

@Data
public class RandomStuff {
	private String message;

	public RandomStuff(String message) {
		this.message = message;
	}

	public RandomStuff() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
