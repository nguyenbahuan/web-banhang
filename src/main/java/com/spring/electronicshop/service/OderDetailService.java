package com.spring.electronicshop.service;

import org.springframework.stereotype.Service;

import com.spring.electronicshop.model.OdersDetails;
import com.spring.electronicshop.repository.OderDetailRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OderDetailService {

	private final OderDetailRepository detailRepository;

	public void createOderDetails(OdersDetails details) {

		detailRepository.save(details);
	}
}
