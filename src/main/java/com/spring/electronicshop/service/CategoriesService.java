package com.spring.electronicshop.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.spring.electronicshop.controller.api.admin.category.FormCategory;
import com.spring.electronicshop.controller.api.admin.category.FormSearchCategory;
import com.spring.electronicshop.dto.CategoriesDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Categories;
import com.spring.electronicshop.repository.CategoriesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoriesService {

	private final CategoriesRepository categoriesRepository;

	private final ModelMapper modelMapper;

	public Optional<CategoriesDTO> findById(Long id) {
		CategoriesDTO dto = modelMapper.map(categoriesRepository.findById(id).get(), CategoriesDTO.class);
		return Optional.of(dto);
	}

	public List<CategoriesDTO> getAllCategories() {
		List<CategoriesDTO> categoriesDTOs = new ArrayList<>();
		List<Categories> categories = categoriesRepository.findAll();
		categories.forEach(c -> {
			categoriesDTOs.add(modelMapper.map(c, CategoriesDTO.class));
		});
		return categoriesDTOs;
	}

	public List<CategoriesDTO> getAllCategoriesUser() {
		List<CategoriesDTO> categoriesDTOs = new ArrayList<>();
		List<Categories> categories = categoriesRepository.findAllCategoryUser();
		categories.forEach(c -> {
			categoriesDTOs.add(modelMapper.map(c, CategoriesDTO.class));
		});
		return categoriesDTOs;
	}

	public ErrorMessage createCategories(FormCategory formCategory, BindingResult bindingResult) {
		ErrorMessage reqRes = new ErrorMessage();
		try {
			if (bindingResult.hasErrors()) {
				reqRes.setMessage(bindingResult.getFieldError().getDefaultMessage());
				reqRes.setStatusCode(0);
			} else {
				Categories categories = modelMapper.map(formCategory, Categories.class);
				categories.setActive(true);
				categories.setCreatedDate(LocalDateTime.now());
				categories.setUpdatedDate(LocalDateTime.now());
				categoriesRepository.save(categories);
				reqRes.setMessage("thêm danh mục thành công");
				reqRes.setStatusCode(1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			reqRes.setMessage(e.getMessage());
			reqRes.setStatusCode(500);
		}

		return reqRes;
	}

	public ErrorMessage updateCategories(Long categoryId, FormCategory formCategory, BindingResult bindingResult) {
		ErrorMessage message = new ErrorMessage();
		Optional<Categories> optional = categoriesRepository.findById(categoryId);
		if (optional.isPresent()) {
			try {
				if (bindingResult.hasErrors()) {
					message.setMessage(bindingResult.getFieldError().getDefaultMessage());
					message.setStatusCode(0);
					return message;
				} else {
					optional.get().setName(formCategory.getName());
					optional.get().setUpdatedDate(LocalDateTime.now());
					categoriesRepository.save(optional.get());
					message.setMessage("sửa danh mục thành công");
					message.setStatusCode(1);
					return message;
				}
			} catch (Exception e) {
				// TODO: handle exception
				message.setMessage(e.getMessage());
				return message;
			}
		}
		message.setMessage("không có id danh mục");

		return message;
	}

	public ErrorMessage deleteCategories(Long categoryId) {
		ErrorMessage message = new ErrorMessage();
		Optional<Categories> optional = categoriesRepository.findById(categoryId);
		if (optional.isPresent()) {
			try {

				optional.get().setActive(false);
				categoriesRepository.save(optional.get());
				message.setMessage("xóa danh mục thành công");
				return message;

			} catch (Exception e) {
				// TODO: handle exception
				message.setMessage(e.getMessage());
				return message;
			}
		}
		message.setMessage("không có id danh mục");

		return message;
	}

	public long countRepo() {

		return categoriesRepository.count();
	}

	public List<CategoriesDTO> searchCategories( PageRequest pageRequest,FormSearchCategory sreachCategory) {
		List<CategoriesDTO> categoriesDTOs = new ArrayList<>();
		List<Categories> categories = categoriesRepository.seachCategory(sreachCategory,pageRequest);
		categories.forEach(c -> {
			categoriesDTOs.add(modelMapper.map(c, CategoriesDTO.class));
		});
		return categoriesDTOs;
	}
}
