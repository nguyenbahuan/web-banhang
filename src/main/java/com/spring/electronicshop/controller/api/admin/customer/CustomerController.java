package com.spring.electronicshop.controller.api.admin.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.controller.api.admin.oder.SearchOder;
import com.spring.electronicshop.dto.OdersDTO;
import com.spring.electronicshop.dto.PaginationDTO;
import com.spring.electronicshop.dto.UserDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.repository.UserRepository;
import com.spring.electronicshop.service.OdersService;
import com.spring.electronicshop.service.UserService;
import com.spring.electronicshop.util.JsonResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/customers")
public class CustomerController {

	private final UserService userService;

	private final OdersService odersService;

	private final UserRepository userRepository;

	@PostMapping("get-all")
	public ResponseEntity<?> getAllUser(@RequestBody SearchCustomer searchOder) {
		PaginationDTO<List<UserDTO>> paginationDTO = new PaginationDTO<List<UserDTO>>();
		int page = searchOder.getPage();
		int size = searchOder.getPageSize();
		long count = userService.countCustomer();
		long total = ((count + size - 1) / size);
		PageRequest pageRequest = PageRequest.of(page, size);
		paginationDTO.setContent(userService.getAllCustomer(pageRequest));
		paginationDTO.setPage(page);
		paginationDTO.setTotal(total);
		return ResponseEntity.ok(JsonResult.success(paginationDTO));
	}

	@GetMapping("detail/{userId}")
	public ResponseEntity<?> getUserDetail(@PathVariable(name = "userId") Long userId) {

		return ResponseEntity.ok(JsonResult.success(userService.findUserbyId(userId)));
	}

	@GetMapping("get-quantity")
	public ResponseEntity<?> getQuantityUser() {
		return ResponseEntity.ok(JsonResult.success(userService.countCustomer()));
	}

	@PostMapping("detail/{userId}/oder")
	public ResponseEntity<?> getUserOder(@PathVariable(name = "userId") Long userId,
			@RequestBody SearchOder searchOder) {
		Optional<User> optional = userRepository.findById(userId);

		if (optional.isPresent()) {
			int page = searchOder.getPage();
			int pageSize = searchOder.getPageSize();
			Long count = odersService.oderCountByUser(optional.get());
			PageRequest pageRequest = PageRequest.of(page, pageSize);
			PaginationDTO<List<OdersDTO>> paginationDTO = new PaginationDTO<List<OdersDTO>>();
			paginationDTO.setContent(odersService.getAllOder(pageRequest, optional.get()));
			paginationDTO.setPage(page);
			paginationDTO.setTotal((count + pageSize - 1) / pageSize);
			return ResponseEntity.ok(JsonResult.success(paginationDTO));
		}
		ErrorMessage message = new ErrorMessage();
		message.setMessage("Người dùng không hợp lệ");
		return ResponseEntity.ok(JsonResult.success(message));
	}

	@DeleteMapping("delete/{userId}")
	public ResponseEntity<?> deleteCustomer(@PathVariable(name = "userId") Long userId) {

		return ResponseEntity.ok(JsonResult.success(userService.deleteUser(userId)));
	}
}
