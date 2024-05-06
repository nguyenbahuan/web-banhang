package com.spring.electronicshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.electronicshop.model.Discounts;

@Repository
public interface DiscountRepository extends JpaRepository<Discounts, Long> {

}
