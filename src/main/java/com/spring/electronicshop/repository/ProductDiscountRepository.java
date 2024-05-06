package com.spring.electronicshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.electronicshop.model.Discounts;
import com.spring.electronicshop.model.ProductDiscount;
import com.spring.electronicshop.model.Products;

import jakarta.transaction.Transactional;

@Repository
public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
	List<ProductDiscount> findByProducts(Products products);

	List<ProductDiscount> findByDiscounts(Discounts discounts);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM ProductDiscount p WHERE p.discounts.id = :discountId")
    void deleteByDiscounts(@Param("discountId") Long discountId);
}
