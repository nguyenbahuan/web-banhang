package com.spring.electronicshop.controller.password;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.ForgotPassword;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.service.ForgotPasswordService;
import com.spring.electronicshop.service.UserService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ResetPasswordController {

	private final PasswordEncoder encoder;

	private final UserService userService;

	private final ForgotPasswordService forgotPasswordService;

	@GetMapping("reset-password")
	public ResponseEntity<?> getMethodName(@RequestParam(name = "token") String token,
			@RequestParam(name = "email") String email) {
		ErrorMessage message = new ErrorMessage();
		Optional<ForgotPassword> forgotPassword = forgotPasswordService.getToken(token);
		if (!forgotPassword.isPresent() || forgotPassword.get().isUsed()) {

			message.setMessage("token không hợp lệ");
			return ResponseEntity.ok(JsonResult.success(message));
		} else if (ChronoUnit.MINUTES.between(forgotPassword.get().getExpireTime(), LocalDateTime.now()) > 5) {
			message.setMessage("token hết hạn");
			return ResponseEntity.ok(JsonResult.success(message));
		}
		if (!forgotPassword.get().getUser().getEmail().equals(email)) {
			message.setMessage("Email không hợp lệ");
			return ResponseEntity.ok(JsonResult.success(message));

		}
		message.setMessage("Nhập mật khẩu mới của bạn");
		message.setStatusCode(1);
		return ResponseEntity.ok(JsonResult.success(message));
	}

	@PostMapping("reset-password")
	public ResponseEntity<?> handlerResetpassword(@Valid @RequestBody FormResetPassword password,
			BindingResult bindingResult, @RequestParam(name = "email") String email,
			@RequestParam(name = "token") String token) {
		ErrorMessage message = new ErrorMessage();

		if (bindingResult.hasErrors()) {
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			message.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(message));
		}
		Optional<User> user = userService.findUserbyEmail(email);
		Optional<ForgotPassword> passwordToken = forgotPasswordService.getToken(token);
		if (user.isPresent() && passwordToken.isPresent()) {

			user.get().setPassword(encoder.encode(password.getPassword()));
			userService.saveUser(user.get());
			passwordToken.get().setUsed(true);
			forgotPasswordService.saveToken(passwordToken.get());
			message.setMessage("Thay đổi mật khẩu thành công");
			message.setStatusCode(1);
			return ResponseEntity.ok(JsonResult.success(message));
		}
		message.setMessage("Thay đổi mật khẩu thất bại");
		message.setStatusCode(0);
		return ResponseEntity.ok(JsonResult.success(message));

	}

}
