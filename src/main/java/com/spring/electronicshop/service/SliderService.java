package com.spring.electronicshop.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.spring.electronicshop.controller.api.admin.slider.FormSlider;
import com.spring.electronicshop.controller.api.admin.slider.FormUpdateSlider;
import com.spring.electronicshop.dto.SliderDTO;
import com.spring.electronicshop.model.Slider;
import com.spring.electronicshop.repository.SliderRepository;
import com.spring.electronicshop.storage.StorageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SliderService {
	private final SliderRepository sliderRepository;
	private final ModelMapper modelMapper;
	private final StorageService storageService;

	public Optional<Slider> getSilderById(Long sliderId) {
		Optional<Slider> optional = sliderRepository.findById(sliderId);
		return optional;
	}

	public List<SliderDTO> getAllSilder(PageRequest pageRequest) {
		return sliderRepository.findAll(pageRequest).stream().map(sl -> modelMapper.map(sl, SliderDTO.class)).toList();

	}
	public List<SliderDTO> getAllSilderUser() {
		return sliderRepository.findAll().stream().map(sl -> modelMapper.map(sl, SliderDTO.class)).toList();

	}

	public void createSlider(FormSlider formSlider) {
		Slider slider = new Slider();
		slider.setContent(formSlider.getContent());
		storageService.store(formSlider.getImages()).forEach(img -> {
			slider.setImage(img);
		});

		sliderRepository.save(slider);

	}

	public void updateSlider(Long sliderId, FormUpdateSlider formSlider) {
		Optional<Slider> slider = sliderRepository.findById(sliderId);
		slider.get().setContent(formSlider.getContent());
		if (formSlider.getImages() != null) {
			storageService.store(formSlider.getImages()).forEach(img -> {
				slider.get().setImage(img);
			});
		}

		sliderRepository.save(slider.get());

	}

	public void deleteSlider(Long sliderId) {

		sliderRepository.deleteById(sliderId);

	}
}
