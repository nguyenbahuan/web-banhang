package com.spring.electronicshop.controller.api.admin.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.dto.CategoriesDTO;
import com.spring.electronicshop.dto.PaginationDTO;
import com.spring.electronicshop.service.CategoriesService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/admin/categories")
public class CategoryController {

	private final CategoriesService categoriesService;

	@GetMapping("detail/{categoryId}")
	public ResponseEntity<?> getCategory(@PathVariable Long categoryId) {

		Optional<CategoriesDTO> categories = categoriesService.findById(categoryId);
		if (categories.isPresent()) {
			return ResponseEntity.ok(JsonResult.success(categories.get()));
		}
		return ResponseEntity.ok(JsonResult.notFound("không tim thấy danh mục"));
	}

	@GetMapping("get-all")
	public ResponseEntity<?> getAllCategory() {
		return ResponseEntity.ok(JsonResult.success(categoriesService.getAllCategories()));
	}

	@PostMapping("search")
	public ResponseEntity<?> searchCategory(@RequestBody FormSearchCategory searchCategory) {
		PaginationDTO<List<CategoriesDTO>> paginationDTO = new PaginationDTO<List<CategoriesDTO>>();
		int page = searchCategory.getPage();
		int size = searchCategory.getPageSize();
		PageRequest pageRequest = PageRequest.of(page, size);
		long count = categoriesService.countRepo();
		long totalPages = (count + size - 1) / size;
		paginationDTO.setTotal(totalPages);
		paginationDTO.setPage(page);
		paginationDTO.setContent(categoriesService.searchCategories(pageRequest, searchCategory));

		return ResponseEntity.ok(JsonResult.success(paginationDTO));
	}

	@PostMapping("create")
	public ResponseEntity<?> createCategory(@Valid @RequestBody FormCategory FormCategory,
			BindingResult bindingResult) {
		return ResponseEntity.ok(JsonResult.success(categoriesService.createCategories(FormCategory, bindingResult)));
	}

	@PutMapping("update/{categoryId}")
	public ResponseEntity<?> updateCategory(@Valid @PathVariable Long categoryId,
			@RequestBody FormCategory formCategory, BindingResult bindingResult) {
		return ResponseEntity
				.ok(JsonResult.success(categoriesService.updateCategories(categoryId, formCategory, bindingResult)));
	}

	@DeleteMapping("delete/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
		return ResponseEntity.ok(JsonResult.success(categoriesService.deleteCategories(categoryId)));
	}

}
