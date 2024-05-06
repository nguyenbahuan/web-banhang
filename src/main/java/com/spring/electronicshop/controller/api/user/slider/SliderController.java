package com.spring.electronicshop.controller.api.user.slider;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.service.SliderService;
import com.spring.electronicshop.util.JsonResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/slider")
public class SliderController {

	private final SliderService sliderService;

	@GetMapping("get-all")
	public ResponseEntity<?> getMethodName() {
		return ResponseEntity.ok(JsonResult.success(sliderService.getAllSilderUser()));
	}

}
