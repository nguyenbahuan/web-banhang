package com.spring.electronicshop.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.electronicshop.controller.api.admin.product.FormSearchProduct;
import com.spring.electronicshop.model.Products;
import com.spring.electronicshop.repository.custom.CustomProductsRepository;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> , CustomProductsRepository{

	@Query("SELECT p FROM Products p WHERE p.isActive = TRUE")
	List<Products> findAllProductUser(Pageable pageRequest);

	@Query("SELECT p FROM Products p LEFT JOIN Categories c On p.id = c.id WHERE c.name = :nameCategory")
	List<Products> findAllByCategory(@Param("nameCategory") String nameCategory);

	@Query("SELECT p FROM Products p WHERE "
			+ "(:#{#searchProduct.getCategoryId()} IS NULL OR p.categories.id = :#{#searchProduct.getCategoryId()}) AND "
			+ "(:#{#searchProduct.getName()} IS NULL OR p.name LIKE %:#{#searchProduct.getName()}%) AND "
			+ "(:#{#searchProduct.getDescription()} IS NULL OR p.description LIKE %:#{#searchProduct.getDescription()}%) AND "
			+ "(:#{#searchProduct.getMinPrice()} IS NULL OR p.price >= :#{#searchProduct.getMinPrice()}) AND "
			+ "(:#{#searchProduct.getMaxPrice()} IS NULL OR p.price <= :#{#searchProduct.getMaxPrice()})  ORDER BY p.createdDate DESC")
	List<Products> searchProducts(@Param("searchProduct") FormSearchProduct searchProduct, Pageable pageRequest);

	@Query("SELECT p FROM Products p WHERE "
			+ "(:#{#searchProduct.getCategoryId()} IS NULL OR p.categories.id = :#{#searchProduct.getCategoryId()}) AND "
			+ "(:#{#searchProduct.getName()} IS NULL OR p.name LIKE %:#{#searchProduct.getName()}%) AND "
			+ "(:#{#searchProduct.getDescription()} IS NULL OR p.description LIKE %:#{#searchProduct.getDescription()}%) AND "
			+ "(:#{#searchProduct.getMinPrice()} IS NULL OR p.price >= :#{#searchProduct.getMinPrice()}) AND "
			+ "(:#{#searchProduct.getMaxPrice()} IS NULL OR p.price <= :#{#searchProduct.getMaxPrice()})  AND p.isActive = TRUE")
	List<Products> searchProductsUser(@Param("searchProduct") FormSearchProduct searchProduct, Pageable pageRequest);

}
