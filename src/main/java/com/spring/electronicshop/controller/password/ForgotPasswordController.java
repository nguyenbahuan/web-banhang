package com.spring.electronicshop.controller.password;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.ForgotPassword;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.service.ForgotPasswordService;
import com.spring.electronicshop.service.UserService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ForgotPasswordController {

	private final UserService userService;

	private final ForgotPasswordService forgotPasswordService;

	@PostMapping("forgot-password")
	public ResponseEntity<?> forgotPassword(@Valid @RequestBody FormForgotPassword email, BindingResult bindingResult)
			throws UnsupportedEncodingException, MessagingException {
		ErrorMessage message = new ErrorMessage();
		if (bindingResult.hasErrors()) {
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(JsonResult.success(message));
		}
		ForgotPassword forgotPassword = new ForgotPassword();
		Optional<User> user = userService.findUserbyEmail(email.getEmail());
		if (!user.isPresent()) {
			message.setMessage("Email không tồn tại");
			message.setStatusCode(200);
			return ResponseEntity.ok(JsonResult.success(message));
		}
		forgotPassword.setToken(forgotPasswordService.generateToken());
		forgotPassword.setExpireTime(forgotPasswordService.expireTime());
		forgotPassword.setUser(user.get());
		forgotPassword.setUsed(false);
		forgotPasswordService.saveToken(forgotPassword);

		String emailLink = "https://shop-huannb.netlify.app" + "/reset-password?token=" + forgotPassword.getToken() + "&email="
				+ email.getEmail();

		message.setMessage("check your email");
		message.setStatusCode(200);
		forgotPasswordService.sendEmail(email.getEmail(), "Reset password", emailLink);
		return ResponseEntity.ok(JsonResult.success(message));
	}

}
