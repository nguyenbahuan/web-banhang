package com.spring.electronicshop.controller.api.user.auth;

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
public class AuthUserController {

	private final LoginService loginService;

	@PostMapping("login")
	public ResponseEntity<?> login(@Valid @RequestBody FormUserLogin loginRequest, BindingResult bindingResult) {

		return ResponseEntity.ok(JsonResult.success(loginService.signInUser(loginRequest, bindingResult)));
	}

	@PostMapping("register")
	public ResponseEntity<?> register(@Valid @RequestBody FormUserRegister request, BindingResult bindingResult) {

		return ResponseEntity.ok(JsonResult.success(loginService.registerUser(request, bindingResult)));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout() {

		return ResponseEntity.ok(JsonResult.success("Logout successful"));
	}

}
