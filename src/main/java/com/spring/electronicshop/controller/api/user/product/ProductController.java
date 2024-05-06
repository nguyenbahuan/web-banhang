package com.spring.electronicshop.controller.api.user.product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.controller.api.admin.product.FormSearchProduct;
import com.spring.electronicshop.controller.api.user.reviews.StatisticalReviewProduct;
import com.spring.electronicshop.dto.CategoriesDTO;
import com.spring.electronicshop.dto.ProductsDTO;
import com.spring.electronicshop.dto.request.PageDTO;
import com.spring.electronicshop.model.ProductDiscount;
import com.spring.electronicshop.model.Products;
import com.spring.electronicshop.service.ProductDiscountService;
import com.spring.electronicshop.service.ProductsService;
import com.spring.electronicshop.service.ReviewsServive;
import com.spring.electronicshop.util.JsonResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/products")
public class ProductController {

	private final ModelMapper modelMapper;

	private final ProductsService productsService;

	private final ProductDiscountService productDiscountService;

	private final ReviewsServive reviewsServive;

	@PostMapping("get-all")
	public ResponseEntity<?> getAllProduct(@RequestBody PageDTO pageDTO) {
		int page = pageDTO.getPage();
		int pageSize = pageDTO.getPageSize();
		PageRequest pageRequest = PageRequest.of(page, pageSize);
		List<ProductsDTO> list = productsService.getAllProductUser(pageRequest);

		return ResponseEntity.ok(JsonResult.success(list));
	}

	@PostMapping("search")
	public ResponseEntity<?> searchProduct(@RequestBody FormSearchProduct searchProduct) {
		int page = searchProduct.getPage();
		int size = searchProduct.getPageSize();
		PageRequest pageRequest = PageRequest.of(page, size);
		searchProduct.setIsActive(true);
		List<ProductsDTO> list = productsService.searchProductsUser(searchProduct, pageRequest);
		return ResponseEntity.ok(JsonResult.success(list));
	}

	@PostMapping("test")
	public ResponseEntity<?> testsearchProduct(@RequestBody FormSearchProduct searchProduct) {
		int page = searchProduct.getPage();
		int size = searchProduct.getPageSize();
		PageRequest pageRequest = PageRequest.of(page, size);
		searchProduct.setIsActive(true);
		List<ProductsDTO> list = productsService.testsearchProductsUser(searchProduct, pageRequest);
		return ResponseEntity.ok(JsonResult.success(list));
	}

	@GetMapping("detail/{productId}")
	public ResponseEntity<?> searchProductCategoty(@PathVariable Long productId) {
		LocalDate date = LocalDate.now();
		Optional<Products> optional = productsService.findById(productId);
		if (optional.isPresent()) {

			List<StatisticalReviewProduct> reviewProducts = reviewsServive
					.satistocalReviewProduct(optional.get().getId());

			double totalRating = 0;
			double coutRating = 0;
			for (StatisticalReviewProduct reviewProduct : reviewProducts) {

				totalRating += reviewProduct.getQuantity() * reviewProduct.getRating();
				coutRating += reviewProduct.getQuantity();
			}
			double rating;
			if (coutRating > 0)
				rating = totalRating / coutRating * 1.0;
			else
				rating = 0;

			List<ProductDiscount> discounts = productDiscountService.getDiscountByProduct(productId);
			double percentage = 0;
			for (ProductDiscount discount : discounts) {
				if ((date.isAfter(discount.getStartDate()) && date.isBefore(discount.getEndDate()))
						|| date.isEqual(discount.getStartDate()) || date.isEqual(discount.getEndDate())) {
					percentage += discount.getDiscounts().getDiscountPercentage();

				}
			}
			ProductsDTO productsDTO = modelMapper.map(optional.get(), ProductsDTO.class);
			productsDTO.setPriceDiscount((long) (optional.get().getPrice() - (optional.get().getPrice() * percentage)));
			productsDTO.setDiscountPercentage((int) (percentage * 100));
			productsDTO.setCategory(modelMapper.map(optional.get().getCategories(), CategoriesDTO.class));
			productsDTO.setRating(rating);
			return ResponseEntity.ok(JsonResult.success(productsDTO));
		}
		return ResponseEntity.ok(JsonResult.notFound("not found"));
	}

}
