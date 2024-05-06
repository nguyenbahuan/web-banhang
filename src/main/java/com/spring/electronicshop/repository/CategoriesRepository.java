package com.spring.electronicshop.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.electronicshop.controller.api.admin.category.FormSearchCategory;
import com.spring.electronicshop.model.Categories;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {
//	public Optional<Categories> findById(Long id);
	@Query("SELECT c FROM Categories c WHERE c.isActive = TRUE")
	List<Categories> findAllCategoryUser();

	@Query("SELECT c FROM Categories c WHERE (:#{#searchCategory.getName()} IS NULL OR c.name = :#{#searchCategory.getName()})")
	List<Categories> seachCategory(@Param("searchCategory") FormSearchCategory searchCategory, Pageable pageRequest);
}
