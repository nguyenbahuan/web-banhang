package com.spring.electronicshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.electronicshop.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
