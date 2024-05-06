package com.spring.electronicshop.service;

import java.text.DecimalFormat;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.spring.electronicshop.controller.api.admin.reviews.ReviewInfo;
import com.spring.electronicshop.controller.api.user.reviews.FormSearchReviews;
import com.spring.electronicshop.controller.api.user.reviews.StatisticalReviewProduct;
import com.spring.electronicshop.dto.PaginationDTO;
import com.spring.electronicshop.dto.ProductReviewsDTO;
import com.spring.electronicshop.model.ProductReviews;
import com.spring.electronicshop.repository.ReviewsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewsServive {

	private final ReviewsRepository reviewsRepository;
	private final ModelMapper modelMapper;

	public void createReview(ProductReviews productReviews) {
		reviewsRepository.save(productReviews);
	}

	public List<StatisticalReviewProduct> satistocalReviewProduct(Long id) {
		List<StatisticalReviewProduct> productReviews = reviewsRepository.statisticalReviewView(id);
		return productReviews;
	}

	public ReviewInfo reviewsAll() {
		ReviewInfo reviewInfo = new ReviewInfo();
		Long count = reviewsRepository.count();
		List<ProductReviews> productReviews = reviewsRepository.findAll();
		Long total = (long) 0;
		for (ProductReviews reviews : productReviews) {
			total += reviews.getRating();
		}
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		Double totalRate = (double) (total / (double) count);
		reviewInfo.setTotalReview(count);
		reviewInfo.setRating(Double.valueOf(decimalFormat.format(totalRate)));
		return reviewInfo;
	}

	public List<ProductReviewsDTO> getAllReviewsProduct(PageRequest pageRequest, Long productId) {
		List<ProductReviews> productReviews = reviewsRepository.getAllReviewsProduct(pageRequest, productId);
		List<ProductReviewsDTO> reviewsDTOs = productReviews.stream()
				.map(pr -> modelMapper.map(pr, ProductReviewsDTO.class)).toList();

		return reviewsDTOs;
	}

	public PaginationDTO<List<ProductReviewsDTO>> getAllReviews(@RequestBody FormSearchReviews formSearchReviews) {
		PaginationDTO<List<ProductReviewsDTO>> paginationDTO = new PaginationDTO<List<ProductReviewsDTO>>();
		int page = formSearchReviews.getPage();
		int pageSize = formSearchReviews.getPageSize();
		Long count = reviewsRepository.count();

		PageRequest pageRequest = PageRequest.of(page, pageSize);
		List<ProductReviews> productReviews = reviewsRepository.searchAll(pageRequest, formSearchReviews);
		List<ProductReviewsDTO> reviewsDTOs = productReviews.stream()
				.map(pr -> modelMapper.map(pr, ProductReviewsDTO.class)).toList();

		paginationDTO.setTotal((count + pageSize - 1) / pageSize);
		paginationDTO.setPage(page);
		paginationDTO.setContent(reviewsDTOs);
		return paginationDTO;
	}
}
