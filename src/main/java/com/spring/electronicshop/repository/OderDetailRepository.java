package com.spring.electronicshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.electronicshop.controller.api.admin.statistical.FormStatisticalByCategory;
import com.spring.electronicshop.model.OdersDetails;

@Repository
public interface OderDetailRepository extends JpaRepository<OdersDetails, Long>{
	@Query("SELECT new com.spring.electronicshop.controller.api.admin.statistical.FormStatisticalByCategory(c.id, c.name, Sum(od.subPrice * od.quantity)) "
			+ "FROM OdersDetails od LEFT JOIN od.oders o LEFT JOIN od.products p LEFT JOIN p.categories c WHERE o.status = 4  "
			+ "GROUP BY c.id")
	List<FormStatisticalByCategory> totalByCategory();
}
