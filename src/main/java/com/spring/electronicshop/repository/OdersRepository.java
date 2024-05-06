package com.spring.electronicshop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.electronicshop.controller.api.admin.statistical.FormStatisticalByYear;
import com.spring.electronicshop.model.Oders;
import com.spring.electronicshop.model.User;

@Repository
public interface OdersRepository extends JpaRepository<Oders, Long> {

	List<Oders> findByUser(Pageable pageable, User user);

	@Query("SELECT o FROM Oders o WHERE o.user.id = :userId")
	List<Oders> findOrderByUser(@Param("userId") Long userId);

	Page<Oders> findAll(Pageable pageRequest);

	@Query("SELECT o FROM Oders o WHERE o.status = 4 AND o.user.id = :userId")
	List<Oders> findOderHistory(@Param("userId") Long userId);

	@Query("SELECT COUNT(o) FROM Oders o WHERE o.user.id = :userId")
	Long countByUser(@Param("userId") Long userId);

	@Query("SELECT new com.spring.electronicshop.controller.api.admin.statistical.FormStatisticalByYear(MONTH(o.createdDate),YEAR(o.createdDate),Sum(o.total)) "
			+ "FROM Oders o WHERE YEAR(o.createdDate) = :year AND o.status = 4 "
			+ "GROUP BY YEAR(o.createdDate), MONTH(o.createdDate) "
			+ "ORDER BY YEAR(o.createdDate), MONTH(o.createdDate)")
	List<FormStatisticalByYear> totalByYear(@Param("year") int year);

	@Query("SELECT Sum(o.total) FROM Oders o WHERE o.status = 4")
	Long totalSales();

	@Query("SELECT Sum(o.total) FROM Oders o WHERE o.status = 4 AND MONTH(o.createdDate) = :month AND YEAR(o.createdDate) = YEAR(CURDATE())")
	Long totalByThisMonth(@Param("month") int month);

	@Query("SELECT COUNT(o) FROM Oders o WHERE o.status = 4")
	Long totalOrder();
	@Query("SELECT COUNT(o) FROM Oders o WHERE o.status = 4 AND MONTH(o.createdDate) = MONTH(CURDATE())")
	Long totalOrderThisMonth();

}
