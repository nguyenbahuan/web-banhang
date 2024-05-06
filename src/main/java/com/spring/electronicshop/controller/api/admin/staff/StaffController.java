package com.spring.electronicshop.controller.api.admin.staff;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.controller.api.user.profile.FormChangePass;
import com.spring.electronicshop.dto.PaginationDTO;
import com.spring.electronicshop.dto.UserDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Role;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.repository.RoleRepository;
import com.spring.electronicshop.repository.UserRepository;
import com.spring.electronicshop.service.LoginService;
import com.spring.electronicshop.service.UserService;
import com.spring.electronicshop.storage.StorageService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/staffs")
public class StaffController {

	private final UserService userService;

	private final LoginService loginService;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;
	
	private final StorageService storageService;
	
	private final RoleRepository roleRepository;

	@PostMapping("create")
	public ResponseEntity<?> createStaff(@Valid @ModelAttribute FormStaff formStaff, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ErrorMessage message = new ErrorMessage();
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			message.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(message));
		}
		return ResponseEntity.ok(JsonResult.success(loginService.registerStaff(formStaff, bindingResult)));
	}
	@PostMapping("delete/{userId}")
	public ResponseEntity<?> deleteStaff(@PathVariable(name = "userId") Long userId) {
		
		return ResponseEntity.ok(JsonResult.success(userService.deleteUser(userId)));
	}

	@PostMapping("get-all")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> getAllStaff(@RequestBody FormSearchStaff formSearchStaff) {
		PaginationDTO<List<UserDTO>> paginationDTO = new PaginationDTO<List<UserDTO>>();
		int size = formSearchStaff.getPageSize();
		int page = formSearchStaff.getPage();
		Long count = userRepository.countByStaff();
		PageRequest pageRequest = PageRequest.of(page, size);
		paginationDTO.setTotal((count + size - 1) / size);
		paginationDTO.setPage(page);
		paginationDTO.setContent(userService.getAllStaff(formSearchStaff, pageRequest));
		return ResponseEntity.ok(JsonResult.success(paginationDTO));
	}

	@GetMapping("profile")
	public ResponseEntity<?> getProfile(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(JsonResult.success(userService.getProfile(user.getId())));
	}

	@PutMapping("profile/update")
	public ResponseEntity<?> updateProfileStaff(@Valid @RequestBody UpdateStaff formStaff,
			Authentication authentication, BindingResult bindingResult) {
		ErrorMessage message = new ErrorMessage();
		if (bindingResult.hasErrors()) {
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(JsonResult.success(message));
		}
		User user = (User) authentication.getPrincipal();
		user.setUpdatedDate(LocalDateTime.now());
		user.setName(formStaff.getName());
		user.setPhoneNumber(formStaff.getPhoneNumber());
		userService.updateProfile(user);
		message.setMessage("update thanh cong");
		return ResponseEntity.ok(JsonResult.success(message));
	}

	@PostMapping("profile/change-password")
	public ResponseEntity<?> changePassword(Authentication authentication, @RequestBody FormChangePass formChangePass) {
		ErrorMessage message = new ErrorMessage();
		User user = (User) authentication.getPrincipal();
		formChangePass.setUserId(user.getId());
		if (!passwordEncoder.matches(formChangePass.getOldPassword(), user.getPassword())) {
			message.setMessage("Incorrect password");
			return ResponseEntity.ok(JsonResult.success(message));
		} else if (!formChangePass.getConfirmPassword().equals(formChangePass.getNewPassword())) {
			message.setMessage("Incorrect confirm password");
			return ResponseEntity.ok(JsonResult.success(message));
		}
		message.setMessage("Change password succses");
		userService.changePassword(user, passwordEncoder.encode(formChangePass.getNewPassword()));
		return ResponseEntity.ok(JsonResult.success(message));
	}

	@GetMapping("show/{userId}")
	public ResponseEntity<?> getProfileStaff(@PathVariable Long userId) {

		return ResponseEntity.ok(JsonResult.success(userService.getProfile(userId)));
	}

	@PostMapping("show/{userId}/update")
	public ResponseEntity<?> updateStaff(@PathVariable("userId") Long userId,
			@Valid @ModelAttribute UpdateStaff updateStaff, BindingResult bindingResult) {

		ErrorMessage message = new ErrorMessage();
		if (bindingResult.hasErrors()) {
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(JsonResult.success(message));
		} else {
			Optional<User> user = userService.loadUserById(userId);
			Optional<Role> role = roleRepository.findById(updateStaff.getRoleId());
			if (user.isPresent()) {
				user.get().setName(updateStaff.getName());
				user.get().setPhoneNumber(updateStaff.getPhoneNumber());
				user.get().setUpdatedDate(LocalDateTime.now());
				user.get().setActive(updateStaff.getActive());
				user.get().setRole(role.get());
				if(updateStaff.getAvatar() != null) {
					storageService.store(updateStaff.getAvatar()).forEach(img -> {
						user.get().setAvatar(img);
					});
				}
				
				
				userService.updateProfile(user.get());
				message.setMessage("Cập nhật thành công");
				message.setStatusCode(1);
				return ResponseEntity.ok(JsonResult.success(message));
			}
			message.setMessage("Nhân viên không tồn tại");
			message.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(message));

		}

	}

}
