package com.spring.electronicshop.controller.api.admin.role;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.service.RoleService;
import com.spring.electronicshop.util.JsonResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin/roles")
public class RoleController {

	private final RoleService roleService;

	@GetMapping("get-all")
	public ResponseEntity<?> requestMethodName() {

		return ResponseEntity.ok(JsonResult.success(roleService.getAllRole()));
	}

}
