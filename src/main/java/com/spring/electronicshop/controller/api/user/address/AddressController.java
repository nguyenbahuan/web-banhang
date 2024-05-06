package com.spring.electronicshop.controller.api.user.address;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.dto.AddressDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Address;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.service.AddressService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/address")
public class AddressController {

	private final AddressService addressService;
	private final ModelMapper modelMapper;

	@PostMapping("create")
	public ResponseEntity<?> createAddress(@Valid @RequestBody FormAddress formAddress, BindingResult bindingResult,
			Authentication authentication) {

		User user = (User) authentication.getPrincipal();
		if (bindingResult.hasErrors()) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(bindingResult.getFieldError().getDefaultMessage());
			errorMessage.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(errorMessage));
		}
		formAddress.setUserId(user.getId());
		return ResponseEntity.ok(JsonResult.success(addressService.createAddress(formAddress)));

	}

	@GetMapping("show/{addressId}")
	public ResponseEntity<?> getAddress(@PathVariable(name = "addressId") Long addressId) {

		Optional<Address> optional = addressService.getAddress(addressId);
		if (optional.isPresent()) {
			AddressDTO dto = modelMapper.map(optional.get(), AddressDTO.class);
			return ResponseEntity.ok(JsonResult.success(dto));
		}
		ErrorMessage message = new ErrorMessage();
		message.setMessage("Không tìm thấy địa chỉ");
		return ResponseEntity.ok(JsonResult.success(message));

	}

	@GetMapping("get-all")
	public ResponseEntity<?> getAllAddress(Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(JsonResult.success(addressService.getAllAddress(user)));

	}

	@PutMapping("update/{addressId}")
	public ResponseEntity<?> updaetAddress(@Valid @PathVariable Long addressId,
			@Valid @RequestBody FormAddress formAddress, BindingResult bindingResult, Authentication authentication) {

		if (bindingResult.hasErrors()) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(bindingResult.getFieldError().getDefaultMessage());
			errorMessage.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(errorMessage));
		}
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(JsonResult.success(addressService.updateAddress(addressId, formAddress, user)));

	}

	@DeleteMapping("delete/{addressId}")
	public ResponseEntity<?> deleteAddress(@Valid @PathVariable Long addressId, Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(JsonResult.success(addressService.deleteAddress(addressId, user)));

	}
}
