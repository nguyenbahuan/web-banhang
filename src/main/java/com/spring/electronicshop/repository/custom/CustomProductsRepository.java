package com.spring.electronicshop.repository.custom;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.spring.electronicshop.controller.api.admin.product.FormSearchProduct;
import com.spring.electronicshop.model.Products;

public interface CustomProductsRepository {
	List<Products> searchProductsbyUser(FormSearchProduct searchProduct,Pageable pageable); 
	List<Products> searchProductsbyAdmin(FormSearchProduct searchProduct,Pageable pageable); 
}
