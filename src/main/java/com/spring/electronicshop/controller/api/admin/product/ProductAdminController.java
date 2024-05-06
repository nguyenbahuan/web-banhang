package com.spring.electronicshop.controller.api.admin.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.aspect.LogExecutionTime;
import com.spring.electronicshop.dto.CategoriesDTO;
import com.spring.electronicshop.dto.PaginationDTO;
import com.spring.electronicshop.dto.ProductsDTO;
import com.spring.electronicshop.model.Products;
import com.spring.electronicshop.service.ProductsService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/admin/products")
public class ProductAdminController {

	private final ModelMapper modelMapper;

	private final ProductsService productsService;

	@GetMapping("get-all")
	public ResponseEntity<?> getPhones() {
		return ResponseEntity.ok(JsonResult.success(productsService.getAllProduct()));
	}

	@PostMapping("search")
	public ResponseEntity<?> searchProduct(@RequestBody FormSearchProduct searchProduct) {

		PaginationDTO<List<ProductsDTO>> paginationDTO = new PaginationDTO<List<ProductsDTO>>();
		int page = searchProduct.getPage();
		int size = searchProduct.getPageSize();
		long count = productsService.countRepo();
		long totalPages = (count + size - 1) / size;
		PageRequest pageRequest = PageRequest.of(page, size);
		List<ProductsDTO> list = productsService.searchProducts(searchProduct, pageRequest);
		paginationDTO.setContent(list);
		paginationDTO.setTotal(totalPages);
		paginationDTO.setPage(page);
		return ResponseEntity.ok(JsonResult.success(paginationDTO));
	}

	@GetMapping
	public List<ProductsDTO> getProductByCategory(
			@RequestParam(name = "category", required = false) String nameCategory) {
		return productsService.getAllProductByCategory(nameCategory);
	}

	@LogExecutionTime
	@GetMapping("detail/{productId}")
	public ResponseEntity<?> getPhoneById(@PathVariable(name = "productId") Long id) {
		Optional<Products> optional = productsService.findById(id);
		if (optional.isPresent()) {
			ProductsDTO productsDTO = modelMapper.map(optional.get(), ProductsDTO.class);
			productsDTO.setCategory(modelMapper.map(optional.get().getCategories(), CategoriesDTO.class));
//			logger.info("Products: " + productsDTO);
			return ResponseEntity.ok(JsonResult.success(productsDTO));
		}
		return ResponseEntity.ok(JsonResult.notFound("not found"));
	}

	@PostMapping("create")
	public ResponseEntity<?> createProduct(@Valid @ModelAttribute FormProduct product, BindingResult bindingResult) {

		return ResponseEntity.ok(JsonResult.success(productsService.createProduct(product, bindingResult)));

	}

	@PutMapping(path = "update/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable(name = "productId") Long id,
			@Valid @ModelAttribute FormUpdateProduct product, BindingResult bindingResult) {
		product.setId(id);
		return ResponseEntity.ok(JsonResult.success(productsService.update(product, bindingResult)));
	}

	@DeleteMapping(path = "delete/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable(name = "productId") Long id) {

		return ResponseEntity.ok(JsonResult.success(productsService.delete(id)));
	}

}
