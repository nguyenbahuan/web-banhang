package com.spring.electronicshop.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.spring.electronicshop.controller.api.admin.discount.FormDiscount;
import com.spring.electronicshop.dto.DiscountsDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Discounts;
import com.spring.electronicshop.repository.DiscountRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DiscountService {
	private final DiscountRepository discountRepository;
	private final ModelMapper modelMapper;

	public ErrorMessage createDiscount(FormDiscount formDiscount) {
		ErrorMessage message = new ErrorMessage();
		Discounts discounts = modelMapper.map(formDiscount, Discounts.class);
		discounts.setActive(true);
		discounts.setCreatedDate(LocalDateTime.now());
		discounts.setUpdatedDate(LocalDateTime.now());
		discountRepository.save(discounts);

		message.setMessage("them thanh cong");
		message.setStatusCode(1);
		return message;
	}
	
	
	public Optional<Discounts> getDiscount( Long id) {
		 Optional<Discounts> discounts = discountRepository.findById(id);
		return discounts;
	}
	
	public List<DiscountsDTO> getAllDiscount(PageRequest pageRequest) {
		List<DiscountsDTO> discountsDTOs = discountRepository.findAll(pageRequest).stream()
				.map(dis -> modelMapper.map(dis, DiscountsDTO.class)).toList();
		return discountsDTOs;
	}

	public ErrorMessage updateDiscount(Long discountId, FormDiscount formDiscount) {
		ErrorMessage message = new ErrorMessage();
		Optional<Discounts> discountsOp = discountRepository.findById(discountId);
		if (discountsOp.isPresent()) {
			Discounts discounts = discountsOp.get();
			modelMapper.map(formDiscount, discounts);
			discounts.setUpdatedDate(LocalDateTime.now());
			discountRepository.save(discounts);

			message.setMessage("sua thanh cong");
			message.setStatusCode(1);
			return message;

		}

		message.setMessage("khong tim thay discount");
		message.setStatusCode(0);
		return message;
	}

	public ErrorMessage deleteDiscount(Long discountId) {
		// TODO Auto-generated method stub
		ErrorMessage message = new ErrorMessage();
		Optional<Discounts> discountsOp = discountRepository.findById(discountId);
		if (discountsOp.isPresent()) {
			Discounts discounts = discountsOp.get();
			discounts.setActive(false);
			discountRepository.save(discounts);

			message.setMessage("xoa thanh cong");
			message.setStatusCode(1);
			return message;

		}
		message.setMessage("khong tim thay discount");
		message.setStatusCode(0);
		return message;
	}
}
