package com.spring.electronicshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.dto.RefreshTokenDTO;
import com.spring.electronicshop.jwt.JWTUtils;
import com.spring.electronicshop.jwt.JwtUser;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.util.JsonResult;

@RestController
@RequestMapping("checkout-token")
public class CheckoutToken {

	@Autowired
	private JWTUtils jwtUtils;

	@PostMapping
	public ResponseEntity<?> postMethodName(@RequestBody RefreshTokenDTO token, Authentication authentication) {
		JwtUser jwtUser = new JwtUser();
		if (authentication == null) {
			ErrorMessage message = new ErrorMessage();
			message.setMessage("User error");
			return ResponseEntity.ok(message);
		}

		User user = (User) authentication.getPrincipal();
		if (jwtUtils.isTokenValid(token.getToken(), user)) {
			jwtUser.setStatusCode(1);
			jwtUser.setError("thành công");
			jwtUser.setTokenAccess(jwtUtils.generateToken(user));
			jwtUser.setEmail(user.getEmail());
			jwtUser.setDob(user.getDob());
			jwtUser.setName(user.getName());
			jwtUser.setPhoneNumber(user.getPhoneNumber());
			jwtUser.setAvatar(user.getAvatar());
			return ResponseEntity.ok(JsonResult.success(jwtUser));
		}
		jwtUser.setError("User có vấn đề");
		return ResponseEntity.ok(JsonResult.success(jwtUser));
	}

}
