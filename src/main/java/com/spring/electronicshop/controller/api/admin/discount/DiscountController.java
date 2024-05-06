package com.spring.electronicshop.controller.api.admin.discount;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.dto.DiscountsDTO;
import com.spring.electronicshop.dto.PaginationDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Discounts;
import com.spring.electronicshop.repository.DiscountRepository;
import com.spring.electronicshop.service.DiscountService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin/discounts")
public class DiscountController {

	private final DiscountService discountService;
	private final DiscountRepository discountRepository;
	private final ModelMapper modelMapper;

	@PostMapping("create")
	public ResponseEntity<?> createDiscount(@Valid @RequestBody FormDiscount formDiscount,
			BindingResult bindingResult) {

		ErrorMessage message = new ErrorMessage();
		if (bindingResult.hasErrors()) {
			message.setStatusCode(0);
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(JsonResult.success(message));
		}

		return ResponseEntity.ok(JsonResult.success(discountService.createDiscount(formDiscount)));
	}

	@GetMapping("show/{discountId}")
	public ResponseEntity<?> getDiscount(@PathVariable(name = "discountId") Long discountId) {

		Optional<Discounts> optional = discountService.getDiscount(discountId);
		if (optional.isPresent()) {
			DiscountsDTO discountsDTO = modelMapper.map(optional.get(), DiscountsDTO.class);
			return ResponseEntity.ok(JsonResult.success(discountsDTO));
		}
		ErrorMessage message = new ErrorMessage();
		message.setStatusCode(0);
		message.setMessage("Không tìm thấy discounts");
		return ResponseEntity.ok(JsonResult.success(message));
	}

	@PostMapping("get-all")
	public ResponseEntity<?> getAllDiscount(@Valid @RequestBody SearchDiscounts searchDiscounts) {
		PaginationDTO<List<DiscountsDTO>> paginationDTO = new PaginationDTO<List<DiscountsDTO>>();
		int page = searchDiscounts.getPage();
		int pageSize = searchDiscounts.getPageSize();
		PageRequest pageRequest = PageRequest.of(page, pageSize);
		Long count = discountRepository.count();
		paginationDTO.setTotal((count + pageSize - 1) / pageSize);
		paginationDTO.setPage(page);
		paginationDTO.setContent(discountService.getAllDiscount(pageRequest));
		return ResponseEntity.ok(JsonResult.success(paginationDTO));
	}

	@PostMapping("update/{discountId}")
	public ResponseEntity<?> updateDiscount(@PathVariable(name = "discountId") Long discountId,
			@Valid @RequestBody FormDiscount formDiscount, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ErrorMessage message = new ErrorMessage();
			message.setStatusCode(0);
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(JsonResult.success(message));
		}
		return ResponseEntity.ok(JsonResult.success(discountService.updateDiscount(discountId, formDiscount)));
	}

	@PostMapping("delete/{discountId}")
	public ResponseEntity<?> deleteDiscount(@PathVariable(name = "discountId") Long discountId) {

		return ResponseEntity.ok(JsonResult.success(discountService.deleteDiscount(discountId)));
	}

}
