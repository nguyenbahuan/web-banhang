package com.spring.electronicshop.controller.api.admin.discount;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.dto.PaginationDTO;
import com.spring.electronicshop.dto.ProductDiscountDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.ProductDiscount;
import com.spring.electronicshop.repository.ProductDiscountRepository;
import com.spring.electronicshop.service.ProductDiscountService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin/product-discount")
public class ProductDiscountController {

	private final ProductDiscountRepository productDiscountRepository;
	private final ProductDiscountService productDiscountService;
	private final ModelMapper modelMapper;

	@PostMapping("create")
	public ResponseEntity<?> createProductDiscount(@Valid @RequestBody FormProductDiscount productDiscount,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(errorMessage);
		}
		return ResponseEntity.ok(JsonResult.success(productDiscountService.createProductDiscount(productDiscount)));
	}

	@PostMapping("store")
	public ResponseEntity<?> createDiscount(@Valid @RequestBody ProductDiscountCreate productDiscount,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(JsonResult.success(errorMessage));
		}
		return ResponseEntity.ok(JsonResult.success(productDiscountService.storeProductDiscount(productDiscount)));
	}

	@PostMapping("update/{discountProductId}")
	public ResponseEntity<?> updateDiscount(@PathVariable(name = "discountProductId") Long discountProductId,
			@Valid @RequestBody FormProductDiscount productDiscount, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(JsonResult.success(errorMessage));
		}
		return ResponseEntity.ok(JsonResult.success(productDiscountService.updateProductDiscount(discountProductId,productDiscount)));
	}

	@PostMapping("get-all")
	public ResponseEntity<?> getAll(@RequestBody SearchDiscounts searchDiscounts) {
		PaginationDTO<List<ProductDiscountDTO>> paginationDTO = new PaginationDTO<List<ProductDiscountDTO>>();
		Long count = productDiscountRepository.count();
		int page = searchDiscounts.getPage();
		int pageSize = searchDiscounts.getPageSize();
		PageRequest pageRequest = PageRequest.of(page, pageSize);
		paginationDTO.setTotal((count + pageSize - 1) / pageSize);
		paginationDTO.setPage(page);
		paginationDTO.setContent(productDiscountService.getAllProductDiscout(pageRequest));
		return ResponseEntity.ok(JsonResult.success(paginationDTO));
	}

	@GetMapping("show/{discountProductId}")
	public ResponseEntity<?> showDiscount(@PathVariable(name = "discountProductId") Long discountProductId) {
		Optional<ProductDiscount> optional = productDiscountService.getDiscountProduct(discountProductId);
		if (optional.isEmpty()) {
			ErrorMessage message = new ErrorMessage();
			message.setMessage("Không tìm thấy discount");
			message.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(message));

		}
		ProductDiscountDTO discountDTO = modelMapper.map(optional.get(), ProductDiscountDTO.class);
		return ResponseEntity.ok(JsonResult.success(discountDTO));
	}

	@DeleteMapping("delete/{discountProductId}")
	public ResponseEntity<?> deleteDiscount(@PathVariable(name = "discountProductId") Long discountProductId) {

		return ResponseEntity.ok(JsonResult.success(productDiscountService.deleteDiscountProduct(discountProductId)));
	}

}
