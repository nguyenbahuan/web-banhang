package com.spring.electronicshop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.electronicshop.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.email = ?1")
	Optional<User> findUserByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.role.name = 'USER'")
	Page<User> findAllCustomer(Pageable pageRequest);

	@Query("SELECT u FROM User u WHERE u.role.name = 'STAFF'")
	Page<User> findAllStaff(Pageable pageRequest);

	@Query("SELECT COUNT(u) FROM User u WhERE u.role.name = 'USER'")
	Long countByCustomers();

	@Query("SELECT COUNT(u) FROM User u WhERE u.role.name = 'STAFF'")
	Long countByStaff();
}
