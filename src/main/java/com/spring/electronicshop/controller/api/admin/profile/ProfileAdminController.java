package com.spring.electronicshop.controller.api.admin.profile;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.controller.api.user.profile.FormChangePass;
import com.spring.electronicshop.dto.UserDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.repository.UserRepository;
import com.spring.electronicshop.service.UserService;
import com.spring.electronicshop.storage.StorageService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/profile")
public class ProfileAdminController {
	
	private final UserService userService;
	private final StorageService storageService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@GetMapping
	public ResponseEntity<?> showProfile(Authentication authentication) {

		User user = (User) authentication.getPrincipal();

		Optional<UserDTO> optional = userService.getProfile(user.getId());
		if (optional.isPresent()) {
			return ResponseEntity.ok(JsonResult.success(optional.get()));
		}
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setMessage("khong tim thay user");
		errorMessage.setStatusCode(0);
		return ResponseEntity.ok(JsonResult.success(errorMessage));
	}
	
	@PostMapping("change-password")
	public ResponseEntity<?> changePassword(Authentication authentication,
			@Valid @RequestBody FormChangePass formChangePass, BindingResult bindingResult) {
		ErrorMessage message = new ErrorMessage();
		if (bindingResult.hasErrors()) {
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			message.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(message));
		}
		User user = (User) authentication.getPrincipal();
		formChangePass.setUserId(user.getId());
		if (!passwordEncoder.matches(formChangePass.getOldPassword(), user.getPassword())) {
			message.setMessage("Mật khẩu không chính xác!!");
			message.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(message));
		} else if (!formChangePass.getConfirmPassword().equals(formChangePass.getNewPassword())) {
			message.setMessage("Xác nhận mật khẩu không chính xác");
			message.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(message));
		}
		message.setMessage("Thay đổi mật khẩu không thành công");
		message.setStatusCode(1);
		userService.changePassword(user, passwordEncoder.encode(formChangePass.getNewPassword()));
		return ResponseEntity.ok(JsonResult.success(message));
	}

	@PutMapping("update")
	public ResponseEntity<?> updateProfile(Authentication authentication,
			@Valid @ModelAttribute FormUpdateProfile formUpdateProfile, BindingResult bindingResult) {
		ErrorMessage message = new ErrorMessage();
		User user = (User) authentication.getPrincipal();
		if (bindingResult.hasErrors()) {
		
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(JsonResult.success(message));
		}
		Optional<UserDTO> optional = userService.getProfile(user.getId());
		if (optional.isPresent()) {
			if(formUpdateProfile.getAvatar() != null) {
				storageService.store(formUpdateProfile.getAvatar()).forEach(img -> {
					user.setAvatar(img);
				});
			}
			
			user.setName(formUpdateProfile.getName());
			userRepository.save(user);
			message.setMessage("cập nhật thành công");
			message.setStatusCode(1);
			return ResponseEntity.ok(JsonResult.success(message));
		}
		
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setMessage("khong tim thay user");
		errorMessage.setStatusCode(0);
		return ResponseEntity.ok(JsonResult.success(errorMessage));
	}
}
