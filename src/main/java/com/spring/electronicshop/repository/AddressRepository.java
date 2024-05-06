package com.spring.electronicshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.electronicshop.model.Address;
import com.spring.electronicshop.model.User;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findByUser(User user);

	@Query("SELECT a FROM Address a WHERE a.isActive = true AND a.user.id = :userId")
	List<Address> findActiveAddresses(@Param("userId") Long userId);
}
