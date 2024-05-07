package com.spring.electronicshop.controller.api.user.category;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.service.CategoriesService;
import com.spring.electronicshop.util.JsonResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/categories")
public class CategoryUserController {
	@Value("${spring.datasource.url}")
	private String database;
	private final CategoriesService categoriesService;

	@GetMapping("get-all")
	public ResponseEntity<?> getMethodName() {

		System.out.println("db " + database);

		return ResponseEntity.ok(JsonResult.success(categoriesService.getAllCategoriesUser()));
	}
}
