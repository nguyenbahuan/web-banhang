package com.spring.electronicshop.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.spring.electronicshop.controller.api.admin.discount.FormDiscount;
import com.spring.electronicshop.controller.api.admin.discount.FormProductDiscount;
import com.spring.electronicshop.controller.api.admin.discount.ProductDiscountCreate;
import com.spring.electronicshop.dto.ProductDiscountDTO;
import com.spring.electronicshop.dto.ProductsDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Discounts;
import com.spring.electronicshop.model.ProductDiscount;
import com.spring.electronicshop.model.Products;
import com.spring.electronicshop.repository.DiscountRepository;
import com.spring.electronicshop.repository.ProductDiscountRepository;
import com.spring.electronicshop.repository.ProductsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductDiscountService {
	private final ModelMapper modelMapper;
	private final ProductsRepository productsRepository;
	private final DiscountRepository discountRepository;
	private final ProductDiscountRepository productDiscountRepository;

	public ErrorMessage createProductDiscount(FormProductDiscount productDiscount) {
		ErrorMessage message = new ErrorMessage();

		Optional<Products> productsOp = productsRepository.findById(productDiscount.getProductId());
		Optional<Discounts> discountOp = discountRepository.findById(productDiscount.getDiscountId());
		if (productsOp.isEmpty() || discountOp.isEmpty()) {
			message.setMessage("productId hoac discountId khong hop le");
			return message;
		}

		List<ProductDiscount> productDiscounts = productDiscountRepository.findByDiscounts(discountOp.get());
		for (ProductDiscount discount : productDiscounts) {
			if (discount.getProducts().getId() == productDiscount.getProductId()) {
				message.setMessage("san pham chi nhan duoc 1 discount nay");
				return message;
			}
		}

		ProductDiscount productDiscountAdd = modelMapper.map(productDiscount, ProductDiscount.class);
		productDiscountAdd.setProducts(productsOp.get());
		productDiscountAdd.setDiscounts(discountOp.get());
		productDiscountRepository.save(productDiscountAdd);
		message.setMessage("them discount thanh cong");
		return message;
	}

	public ErrorMessage storeProductDiscount(ProductDiscountCreate productDiscount) {
		ErrorMessage message = new ErrorMessage();
		List<Products> products = productsRepository.findAllById(productDiscount.getProductId());

		Optional<Discounts> discountOp = discountRepository.findById(productDiscount.getDiscountId());
		if (discountOp.isEmpty()) {
			message.setMessage("productId hoac discountId khong hop le");
			return message;
		}
		productDiscountRepository.deleteByDiscounts(discountOp.get().getId());
		List<ProductDiscount> productDiscounts = productDiscountRepository.findByDiscounts(discountOp.get());
		for (ProductDiscount discount : productDiscounts) {
			for (Long productId : productDiscount.getProductId()) {
				if (discount.getProducts().getId() == productId) {
					message.setMessage("san pham chi nhan duoc 1 discount nay");
					return message;
				}
			}

		}
		if (Period.between(LocalDate.now(), productDiscount.getStartDate()).getDays() < 0) {
			message.setMessage("Ngày bắt đầu không hợp lý"
					+ Period.between(productDiscount.getStartDate(), LocalDate.now()).getDays());
			return message;
		}
		if (Period.between(productDiscount.getStartDate(), productDiscount.getEndDate()).getDays() < 0) {
			message.setMessage("Ngày giảm giá không hợp lệ");
			return message;
		}

		for (Products product : products) {
			ProductDiscount productDiscountAdd = modelMapper.map(productDiscount, ProductDiscount.class);
			productDiscountAdd.setProducts(product);
			productDiscountAdd.setDiscounts(discountOp.get());
			productDiscountRepository.save(productDiscountAdd);
		}
		message.setStatusCode(1);
		message.setMessage("them discount thanh cong");
		return message;
	}

	public ErrorMessage updateProductDiscount(Long discountProductId, FormProductDiscount productDiscount) {
		ErrorMessage message = new ErrorMessage();
		Optional<Products> productsOp = productsRepository.findById(productDiscount.getProductId());
		Optional<Discounts> discountOp = discountRepository.findById(productDiscount.getDiscountId());
		Optional<ProductDiscount> optional = productDiscountRepository.findById(discountProductId);
		if (optional.isEmpty()) {
			message.setMessage("giảm giá không tồn tại");
			return message;
		}
		if (productsOp.isEmpty() || discountOp.isEmpty()) {
			message.setMessage("productId hoac discountId khong hop le");
			return message;
		}
		if (Period.between(LocalDate.now(), productDiscount.getStartDate()).getDays() < 0) {
			message.setMessage("Ngày bắt đầu không hợp lý"
					+ Period.between(productDiscount.getStartDate(), LocalDate.now()).getDays());
			return message;
		}
		if (Period.between(productDiscount.getStartDate(), productDiscount.getEndDate()).getDays() < 0) {
			message.setMessage("Ngày giảm giá không hợp lệ");
			return message;
		}

		optional.get().setProducts(productsOp.get());
		optional.get().setDiscounts(discountOp.get());
		optional.get().setStartDate(productDiscount.getStartDate());
		optional.get().setEndDate(productDiscount.getEndDate());
		productDiscountRepository.save(optional.get());
		message.setStatusCode(1);
		message.setMessage("Cập nhật discount thành công");
		return message;
	}

	public List<ProductDiscountDTO> getAllProductDiscout(PageRequest pageRequest) {
		List<ProductDiscountDTO> discountDTOs = new ArrayList<>();
		productDiscountRepository.findAll(pageRequest).forEach(pd -> {
			ProductDiscountDTO productDiscountDTO = modelMapper.map(pd, ProductDiscountDTO.class);
			productDiscountDTO.setProducts(modelMapper.map(pd.getProducts(), ProductsDTO.class));
			discountDTOs.add(productDiscountDTO);
		});

		return discountDTOs;
	}

	public List<ProductDiscount> getDiscountByProduct(Long productId) {
		Optional<Products> productsOp = productsRepository.findById(productId);
		if (productsOp.isEmpty()) {
			return new ArrayList<ProductDiscount>();
		}
		List<ProductDiscount> discounts = productDiscountRepository.findByProducts(productsOp.get());

		return discounts;

	}

	public ErrorMessage updateDiscount(Long discoutId, FormDiscount discount) {
		ErrorMessage errorMessage = new ErrorMessage();
		Optional<Discounts> optional = discountRepository.findById(discoutId);
		if (optional.isPresent()) {
			optional.get().setDiscountName(discount.getDiscountName());
			optional.get().setDiscountPercentage(discount.getDiscountPercentage());
			discountRepository.save(optional.get());
			errorMessage.setMessage("cap nhat discount thanh cong");
			return errorMessage;
		}
		errorMessage.setMessage("khong tim thay discount");
		return errorMessage;
	}

	public ErrorMessage deleteDiscountProduct(Long discountProductId) {
		ErrorMessage message = new ErrorMessage();
		Optional<ProductDiscount> discountsOp = productDiscountRepository.findById(discountProductId);
		if (discountsOp.isPresent()) {
			productDiscountRepository.deleteById(discountProductId);
			message.setMessage("Xóa thành công");
			message.setStatusCode(1);
			return message;
		}

		message.setMessage("Không tìm thấy đối tượng");
		message.setStatusCode(0);
		return message;
	}

	public Optional<ProductDiscount> getDiscountProduct(Long discountProductId) {

		Optional<ProductDiscount> discountsOp = productDiscountRepository.findById(discountProductId);

		return discountsOp;
	}

}
