package com.spring.electronicshop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.electronicshop.controller.api.user.reviews.FormSearchReviews;
import com.spring.electronicshop.controller.api.user.reviews.StatisticalReviewProduct;
import com.spring.electronicshop.model.ProductReviews;

@Repository
public interface ReviewsRepository extends JpaRepository<ProductReviews, Long> {
	@Query("SELECT r FROM ProductReviews r WHERE r.products.id = :productId AND r.isActive = TRUE ORDER BY r.createdDate DESC")
	List<ProductReviews> getAllReviewsProduct(Pageable pageable, @Param("productId") Long productId);

	@Query("SELECT COUNT(r) FROM ProductReviews r WHERE r.products.id = :productId AND r.isActive = TRUE ")
	Long countProductReview(@Param("productId") Long productId);

	@Query("SELECT r FROM ProductReviews r ORDER BY r.createdDate DESC")
	List<ProductReviews> searchAll(Pageable pageRequest, FormSearchReviews formSearchReviews);

	@Query("SELECT new com.spring.electronicshop.controller.api.user.reviews.StatisticalReviewProduct(r.rating, count(r.rating)) FROM ProductReviews r WHERE r.products.id = :productId GROUP BY r.rating")
	List<StatisticalReviewProduct> statisticalReviewView(@Param("productId") Long productId);
}
