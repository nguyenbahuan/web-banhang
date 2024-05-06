package com.spring.electronicshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.electronicshop.model.Images;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {

	@Query("SELECT i FROM Images i WHERE i.products.id = :productId")
	List<Images> findAllByProduct(@Param("productId") Long productId);

}
