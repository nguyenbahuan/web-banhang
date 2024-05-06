package com.spring.electronicshop.controller.api.user.reviews;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.dto.PaginationDTO;
import com.spring.electronicshop.dto.ProductReviewsDTO;
import com.spring.electronicshop.dto.request.PageDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.ProductReviews;
import com.spring.electronicshop.model.Products;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.repository.ReviewsRepository;
import com.spring.electronicshop.service.ProductsService;
import com.spring.electronicshop.service.ReviewsServive;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewsController {
	private final ReviewsRepository reviewsRepository;

	private final ReviewsServive reviewsServive;

	private final ProductsService productsService;

	private final ModelMapper modelMapper;

	@PostMapping("product/{productId}")
	public ResponseEntity<?> getReviewsProduct(@PathVariable(name = "productId") Long productId,
			@RequestBody PageDTO pageDTO) {
		PaginationDTO<List<ProductReviewsDTO>> paginationDTO = new PaginationDTO<List<ProductReviewsDTO>>();
		int page = pageDTO.getPage();
		int size = pageDTO.getPageSize();
		Long count = reviewsRepository.countProductReview(productId);
		PageRequest pageRequest = PageRequest.of(page, size);
		paginationDTO.setContent(reviewsServive.getAllReviewsProduct(pageRequest, productId));
		paginationDTO.setPage(page);
		paginationDTO.setTotal((count + size - 1) / size);
		return ResponseEntity.ok(JsonResult.success(paginationDTO));
	}

	@GetMapping("statistical/product/{productId}")
	public ResponseEntity<?> statisticalReviewsProduct(@PathVariable(name = "productId") Long productId) {
		List<StatisticalReviewProduct> reviewProducts = new ArrayList<>();
		for (int i = 5; i >= 1; i--) {
			StatisticalReviewProduct reviewProduct = new StatisticalReviewProduct();
			reviewProduct.setQuantity((long) 0);
			reviewProduct.setRating(i);
			reviewProducts.add(reviewProduct);
		}
		List<StatisticalReviewProduct> statisticalReviewProducts = reviewsServive.satistocalReviewProduct(productId);
		for (StatisticalReviewProduct reviewProduct : statisticalReviewProducts) {
			int rating = reviewProduct.getRating();
			if (rating >= 0 && rating <= 5) {

				reviewProducts.set(5 - rating, reviewProduct);
			}
		}
		return ResponseEntity.ok(JsonResult.success(reviewProducts));
	}

	@PostMapping("create")
	public ResponseEntity<?> sendReviews(Authentication authentication, @Valid @RequestBody FormReviews formReviews,
			BindingResult bindingResult) {
		ErrorMessage message = new ErrorMessage();
		User user = (User) authentication.getPrincipal();

		if (bindingResult.hasErrors()) {
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			message.setStatusCode(0);
			return ResponseEntity.ok(JsonResult.success(message));
		}
		Optional<Products> products = productsService.getById(formReviews.getProductId());
		if (products.isPresent()) {
			ProductReviews productReviews = modelMapper.map(formReviews, ProductReviews.class);
			productReviews.setProducts(products.get());
			productReviews.setUser(user);
			productReviews.setCreatedDate(LocalDateTime.now());
			productReviews.setUpdatedDate(LocalDateTime.now());
			productReviews.setActive(true);
			reviewsServive.createReview(productReviews);
			message.setMessage("Đánh giá thành công");
			message.setStatusCode(1);
			return ResponseEntity.ok(JsonResult.success(message));
		}

		message.setMessage("Đánh giá thất bại");
		message.setStatusCode(0);
		return ResponseEntity.ok(JsonResult.success(message));

	}

}
