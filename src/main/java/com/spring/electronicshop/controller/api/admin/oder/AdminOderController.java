package com.spring.electronicshop.controller.api.admin.oder;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.controller.api.user.oder.OderDetailShow;
import com.spring.electronicshop.dto.OdersDTO;
import com.spring.electronicshop.dto.PaginationDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Oders;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.repository.OdersRepository;
import com.spring.electronicshop.service.OdersService;
import com.spring.electronicshop.util.JsonResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin/oders")
public class AdminOderController {

	private final OdersService odersService;

	private final OdersRepository odersRepository;

	@PostMapping("get-all")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STAFF')")
	public ResponseEntity<?> getAllOder(@RequestBody SearchOder searchOder) {
		PaginationDTO<List<OdersDTO>> paginnation = new PaginationDTO<>();
		int page = searchOder.getPage();
		int size = searchOder.getPageSize();
		Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		Long count = odersService.oderCount();
		paginnation.setTotal((count + size - 1) / size);
		paginnation.setPage(page);
		paginnation.setContent(odersService.getAll(searchOder, pageRequest));
		return ResponseEntity.ok(JsonResult.success(paginnation));
	}

	@PatchMapping("update-status/{orderId}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STAFF')")
	public ResponseEntity<?> updateStatusOder(@PathVariable(name = "orderId") Long id,
			@RequestBody UpdateOder updateOder) {

		return ResponseEntity.ok(JsonResult.success(odersService.changeStatus(id, updateOder)));
	}

	@GetMapping("show/{orderId}")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STAFF')")
	public ResponseEntity<?> getOder(@PathVariable(name = "orderId") Long id) {

		OdersDTO dtos = odersService.getOder(id);

		return ResponseEntity.ok(JsonResult.success(dtos));
	}

	@GetMapping("show/{orderId}/detail")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STAFF')")
	public ResponseEntity<?> getOderDetail(Authentication authentication, @PathVariable(name = "orderId") Long id) {
		User user = (User) authentication.getPrincipal();
		Optional<Oders> details = odersRepository.findById(id);
		if (details.isEmpty()) {
			ErrorMessage message = new ErrorMessage();
			message.setMessage("Không tìm thấy đơn hàng");
			message.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(message));
		}
		List<OderDetailShow> dtos = odersService.getOderDetail(user, id);

		return ResponseEntity.ok(JsonResult.success(dtos));
	}
}
