package com.spring.electronicshop.repository.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.spring.electronicshop.controller.api.admin.product.FormSearchProduct;
import com.spring.electronicshop.model.Products;
import com.spring.electronicshop.repository.custom.CustomProductsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

public class ProductsRepositoryImpl implements CustomProductsRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Products> searchProductsbyUser(FormSearchProduct searchProduct, Pageable pageable) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("SELECT p ");
		buffer.append(" FROM Products p ");
		buffer.append(" WHERE p.isActive = TRUE ");

		if (searchProduct.getName() != null) {
			buffer.append(" AND p.name LIKE :nameProduct ");
		}
		if (searchProduct.getCategoryId() != null) {
			buffer.append(" AND p.categories.id = :categoryId ");
		}

		if (searchProduct.getMaxPrice() != null) {
			buffer.append(" AND p.price <= :max ");
		}
		if (searchProduct.getMinPrice() != null) {
			buffer.append(" AND p.price >= :min ");
		}
		if (searchProduct.getDescription() != null) {
			buffer.append(" AND p.description LIKE :description ");
		}
		if (searchProduct.getOrderBy() != null) {
			if (searchProduct.getOrderBy().equals("low price")) {
				buffer.append(" ORDER BY p.price ");
			} else if (searchProduct.getOrderBy().equals("hight price")) {
				buffer.append(" ORDER BY p.price DESC");
			} else if (searchProduct.getOrderBy().equals("new")) {
				buffer.append(" ORDER BY p.createdDate DESC");
			}

		}

		TypedQuery<Products> query = entityManager.createQuery(buffer.toString(), Products.class);

		if (searchProduct.getName() != null) {
			query.setParameter("nameProduct", "%" + searchProduct.getName() + "%");
		}
		if (searchProduct.getCategoryId() != null) {
			query.setParameter("categoryId", searchProduct.getCategoryId());
		}
		if (searchProduct.getMaxPrice() != null) {
			query.setParameter("max", searchProduct.getMaxPrice());
		}
		if (searchProduct.getMinPrice() != null) {
			query.setParameter("min", searchProduct.getMinPrice());
		}
		if (searchProduct.getDescription() != null) {
			query.setParameter("description", "%" + searchProduct.getDescription() + "%");
		}
		query.setFirstResult((searchProduct.getPage()) * searchProduct.getPageSize());
		query.setMaxResults(searchProduct.getPageSize());
		return query.getResultList();

	}

	@Override
	public List<Products> searchProductsbyAdmin(FormSearchProduct searchProduct, Pageable pageable) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("SELECT p ");
		buffer.append(" FROM Products p ");
		buffer.append(" WHERE 1 = 1 ");

		if (searchProduct.getName() != null) {
			buffer.append(" AND p.name LIKE :nameProduct ");
		}
		if (searchProduct.getCategoryId() != null) {
			buffer.append(" AND p.categories.id = :categoryId ");
		}

		if (searchProduct.getMaxPrice() != null) {
			buffer.append(" AND p.price <= :max ");
		}
		if (searchProduct.getMinPrice() != null) {
			buffer.append(" AND p.price => :min ");
		}
		if (searchProduct.getDescription() != null) {
			buffer.append(" AND p.description LIKE :description ");
		}

		buffer.append(" LIMIT :pageSize ");

		TypedQuery<Products> query = entityManager.createQuery(buffer.toString(), Products.class);

		if (searchProduct.getName() != null) {
			query.setParameter("nameProduct", "%" + searchProduct.getName() + "%");
		}
		if (searchProduct.getCategoryId() != null) {
			query.setParameter("categoryId", searchProduct.getCategoryId());
		}
		if (searchProduct.getMaxPrice() != null) {
			query.setParameter("max", searchProduct.getMaxPrice());
		}
		if (searchProduct.getMinPrice() != null) {
			query.setParameter("min", searchProduct.getMinPrice());
		}
		if (searchProduct.getDescription() != null) {
			query.setParameter("description", "%" + searchProduct.getDescription() + "%");
		}
		query.setParameter("pageSize", searchProduct.getPageSize());
		query.setParameter("offset", searchProduct.getOffset());
		return query.getResultList();
	}
}
