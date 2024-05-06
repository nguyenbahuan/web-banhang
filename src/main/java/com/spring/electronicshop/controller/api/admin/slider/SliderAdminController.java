package com.spring.electronicshop.controller.api.admin.slider;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.electronicshop.dto.PaginationDTO;
import com.spring.electronicshop.dto.SliderDTO;
import com.spring.electronicshop.dto.request.PageDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Slider;
import com.spring.electronicshop.repository.SliderRepository;
import com.spring.electronicshop.service.SliderService;
import com.spring.electronicshop.util.JsonResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/admin/slider")
public class SliderAdminController {
	private final SliderService sliderService;
	private final ModelMapper modelMapper;
	private final SliderRepository sliderRepository;

	@PostMapping("get-all")
	public ResponseEntity<?> allSlider(@RequestBody PageDTO pageDTO) {
		PaginationDTO<List<SliderDTO>> paginationDTO = new PaginationDTO<>();
		int page = pageDTO.getPage();
		int size = pageDTO.getPageSize();
		long count = sliderRepository.count();
		long totalPages = (count + size - 1) / size;
		PageRequest pageRequest = PageRequest.of(page, size);
		List<SliderDTO> list = sliderService.getAllSilder(pageRequest);

		paginationDTO.setContent(list);
		paginationDTO.setTotal(totalPages);
		paginationDTO.setPage(page);
		return ResponseEntity.ok(JsonResult.success(paginationDTO));
	}

	@GetMapping("show/{sliderId}")
	public ResponseEntity<?> getSliderById(@PathVariable(name = "sliderId") Long sliderId) {
		Optional<Slider> optional = sliderService.getSilderById(sliderId);
		if (optional.isPresent()) {
			SliderDTO dto = modelMapper.map(optional.get(), SliderDTO.class);
			return ResponseEntity.ok(JsonResult.success(dto));
		}
		ErrorMessage message = new ErrorMessage();
		message.setMessage("Không tìm thấy Slide");
		return ResponseEntity.ok(JsonResult.success(message));

	}

	@PostMapping("create")
	public ResponseEntity<?> createSlider(@Valid @ModelAttribute FormSlider formSlider, BindingResult bindingResult) {
		ErrorMessage message = new ErrorMessage();
		if (bindingResult.hasErrors()) {
			message.setStatusCode(0);
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(JsonResult.success(message));
		}
		sliderService.createSlider(formSlider);
		message.setStatusCode(1);
		message.setMessage("Thêm slider thành công");
		return ResponseEntity.ok(JsonResult.success(message));
	}

	@PutMapping("update/{sliderId}")
	public ResponseEntity<?> updateSlider(@PathVariable(name = "sliderId") Long sliderId,
			@Valid @ModelAttribute FormUpdateSlider formSlider, BindingResult bindingResult) {
		ErrorMessage message = new ErrorMessage();
		if (bindingResult.hasErrors()) {
			message.setStatusCode(0);
			message.setMessage(bindingResult.getFieldError().getDefaultMessage());
			return ResponseEntity.ok(JsonResult.success(message));
		}
		sliderService.updateSlider(sliderId, formSlider);
		message.setStatusCode(1);
		message.setMessage("Sửa slider thành công");
		return ResponseEntity.ok(JsonResult.success(message));
	}

	@DeleteMapping("delete/{sliderId}")
	public ResponseEntity<?> deleteSlider(@PathVariable Long sliderId) {
		ErrorMessage message = new ErrorMessage();
		sliderService.deleteSlider(sliderId);
		message.setStatusCode(1);
		message.setMessage("Xóa slider thành công");
		return ResponseEntity.ok(JsonResult.success(message));
	}
}
