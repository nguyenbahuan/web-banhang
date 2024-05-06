package com.spring.electronicshop.controller.api.user.oder;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.controller.api.admin.oder.UpdateOder;
import com.spring.electronicshop.dto.OdersDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.service.OdersService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/oders")
@PreAuthorize("hasAuthority('USER')")
public class OderController {

	private final OdersService odersService;

	@PostMapping("create")
	public ResponseEntity<?> createOder(@Valid @RequestBody FormOder formOder, BindingResult bindingResult,
			Authentication authentication) {
		if (bindingResult.hasErrors()) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(JsonResult.success(errorMessage));
		}
		User user = (User) authentication.getPrincipal();
		formOder.setUserId(user.getId());
		return ResponseEntity.ok(JsonResult.success(odersService.createOder(formOder)));
	}

	@PostMapping("update-status/{orderId}")
	public ResponseEntity<?> updateStatus(@PathVariable(name = "orderId") Long id, @RequestBody UpdateOder updateOder,
			BindingResult bindingResult) {
		ErrorMessage message = new ErrorMessage();
		if (bindingResult.hasErrors()) {
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			message.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(message));
		}
		return ResponseEntity.ok(JsonResult.success(odersService.changeStatusUser(id, updateOder)));
	}

	@GetMapping("show")
	public ResponseEntity<?> getAllOder(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		List<OdersDTO> dtos = odersService.getAllOrder(user);

		return ResponseEntity.ok(JsonResult.success(dtos));
	}

	@GetMapping("show/{oderId}")
	public ResponseEntity<?> getOder(Authentication authentication, @PathVariable(name = "oderId") Long id) {

		OdersDTO dtos = odersService.getOder(id);

		return ResponseEntity.ok(JsonResult.success(dtos));
	}

	@GetMapping("show/{oderId}/detail")
	public ResponseEntity<?> getOderDetail(Authentication authentication, @PathVariable(name = "oderId") Long id) {
		User user = (User) authentication.getPrincipal();

		List<OderDetailShow> dtos = odersService.getOderDetail(user, id);

		return ResponseEntity.ok(JsonResult.success(dtos));
	}

	@GetMapping("history")
	public ResponseEntity<?> getOderHistory(Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		List<OdersDTO> dtos = odersService.getOderHistory(user);

		return ResponseEntity.ok(JsonResult.success(dtos));
	}
}
