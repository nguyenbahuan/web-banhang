package com.spring.electronicshop.controller.api.admin.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.service.LoginService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthAdminController {
	private final LoginService loginService;

	@PostMapping("/admin/login")
	public ResponseEntity<?> login(@Valid @RequestBody FormLogin requets, BindingResult bindingResult)
			throws Exception {

		return ResponseEntity.ok(JsonResult.success(loginService.signInAdmin(requets, bindingResult)));
	}

	@PostMapping("/admin/register")
	public ResponseEntity<?> signup(@Valid @RequestBody FormRegister request, BindingResult bindingResult) {
		return ResponseEntity.ok(JsonResult.success(loginService.register(request, bindingResult)));
	}
}
