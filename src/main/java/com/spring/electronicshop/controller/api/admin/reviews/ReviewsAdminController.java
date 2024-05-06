package com.spring.electronicshop.controller.api.admin.reviews;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.controller.api.user.reviews.FormSearchReviews;
import com.spring.electronicshop.service.ReviewsServive;
import com.spring.electronicshop.util.JsonResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin/reviews")
public class ReviewsAdminController {
	private final ReviewsServive reviewsServive;

//	private final ProductsService productsService;
//
//	private final ModelMapper modelMapper;

	@PostMapping("get-all")
	public ResponseEntity<?> getReviews(@RequestBody FormSearchReviews formSearchReviews) {

		return ResponseEntity.ok(JsonResult.success(reviewsServive.getAllReviews(formSearchReviews)));
	}
	
	@GetMapping("reviews-info")
	public ResponseEntity<?> getReviewsInfo() {

		return ResponseEntity.ok(JsonResult.success(reviewsServive.reviewsAll()));
	}


}
