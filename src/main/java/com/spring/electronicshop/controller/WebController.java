package com.spring.electronicshop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.message.ErrorMessage;

@RestController
@RequestMapping(path = "/")
public class WebController {
	@GetMapping("/")
	public ErrorMessage home() {
		return new ErrorMessage(200, "hello");
	}

	@GetMapping("home")
	public ErrorMessage homie() {
		return new ErrorMessage(200, "hello");
	}

}
