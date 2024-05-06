package com.spring.electronicshop.controller.api.admin.statistical;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.dto.CategoriesDTO;
import com.spring.electronicshop.repository.OderDetailRepository;
import com.spring.electronicshop.repository.OdersRepository;
import com.spring.electronicshop.service.CategoriesService;
import com.spring.electronicshop.util.JsonResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin/statistical")
public class StatisticalController {

	private Logger logger = LogManager.getLogger(StatisticalController.class);
	private final OdersRepository odersRepository;
	private final OderDetailRepository oderDetailRepository;
	private final CategoriesService categoriesService;

	@GetMapping("by-year/{year}")
	public ResponseEntity<?> satatisticalByYear(@PathVariable int year) {
		List<FormStatisticalByYear> byYears = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			FormStatisticalByYear statisticalByYear = new FormStatisticalByYear();
			statisticalByYear.setMonth(i);
			statisticalByYear.setYear(year);
			statisticalByYear.setTotal(0L);
			byYears.add(statisticalByYear);
		}
		List<FormStatisticalByYear> list = odersRepository.totalByYear(year);
		for (FormStatisticalByYear byYear : list) {
			int month = byYear.getMonth();
			byYears.set(month - 1, byYear);
		}
		return ResponseEntity.ok(JsonResult.success(byYears));
	}

	@GetMapping("by-categories")
	public ResponseEntity<?> satatisticalByCategory() {
		List<FormStatisticalByCategory> byCategories = new ArrayList<>();
		for (CategoriesDTO dto : categoriesService.getAllCategories()) {
			FormStatisticalByCategory statisticalByCategory = new FormStatisticalByCategory();
			statisticalByCategory.setId(dto.getId());
			statisticalByCategory.setName(dto.getName());
			statisticalByCategory.setTotal(0L);
			byCategories.add(statisticalByCategory);
		}
		List<FormStatisticalByCategory> list = oderDetailRepository.totalByCategory();
		for (FormStatisticalByCategory category : list) {
			Long id = category.getId();
			for (int i = 0; i < byCategories.size(); i++) {
				if (byCategories.get(i).getId().equals(id)) {
					byCategories.set(i, category);
				}
			}

		}
		return ResponseEntity.ok(JsonResult.success(byCategories));
	}

	@GetMapping("by-this-month")
	public ResponseEntity<?> satatisticalByMonth() {
		int month = LocalDate.now().getMonthValue();
		logger.info(odersRepository.totalByThisMonth(month));
		return ResponseEntity.ok(JsonResult.success(odersRepository.totalByThisMonth(month)));
	}

	@GetMapping("get-total")
	public ResponseEntity<?> satatisticalTotal() {
		return ResponseEntity.ok(JsonResult.success(odersRepository.totalSales()));
	}

	@GetMapping("get-total-order")
	public ResponseEntity<?> satatisticalTotalOrder() {
		return ResponseEntity.ok(JsonResult.success(odersRepository.totalOrder()));
	}

	@GetMapping("get-month-order")
	public ResponseEntity<?> satatisticalMonthOrder() {
		return ResponseEntity.ok(JsonResult.success(odersRepository.totalOrderThisMonth()));
	}
}
