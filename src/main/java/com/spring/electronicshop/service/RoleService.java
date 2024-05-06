package com.spring.electronicshop.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.spring.electronicshop.dto.RoleDTO;
import com.spring.electronicshop.model.Role;
import com.spring.electronicshop.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService {

	private final RoleRepository roleRepository;

	private final ModelMapper modelMapper;

	public List<RoleDTO> getAllRole() {
		List<Role> list = roleRepository.findAll();
		List<RoleDTO> roleDTOs = list.stream().map(r -> modelMapper.map(r, RoleDTO.class)).toList();

		return roleDTOs;
	}
}
