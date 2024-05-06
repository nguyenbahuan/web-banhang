package com.spring.electronicshop.service;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.spring.electronicshop.controller.api.admin.product.FormProduct;
import com.spring.electronicshop.controller.api.admin.product.FormSearchProduct;
import com.spring.electronicshop.controller.api.admin.product.FormUpdateProduct;
import com.spring.electronicshop.controller.api.user.reviews.StatisticalReviewProduct;
import com.spring.electronicshop.dto.CategoriesDTO;
import com.spring.electronicshop.dto.ProductsDTO;
import com.spring.electronicshop.dto.ReqRes;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Categories;
import com.spring.electronicshop.model.Images;
import com.spring.electronicshop.model.ProductDiscount;
import com.spring.electronicshop.model.Products;
import com.spring.electronicshop.repository.CategoriesRepository;
import com.spring.electronicshop.repository.ImagesRepository;
import com.spring.electronicshop.repository.ProductsRepository;
import com.spring.electronicshop.storage.StorageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductsService {

	private final ProductsRepository productsRepository;

	private final CategoriesRepository categoriesRepository;

	private final ImagesRepository imagesRepository;

	private final ProductDiscountService productDiscountService;

	private final StorageService storageService;

	private final ReviewsServive reviewsServive;

	private final ModelMapper modelMapper;

	public Optional<Products> findById(Long id) {
		Optional<Products> products = productsRepository.findById(id);

		return products;
	}

	public Optional<Products> getById(Long id) {
		Optional<Products> products = productsRepository.findById(id);
		return products;
	}

	public ReqRes delete(Long id) {
		ReqRes reqRes = new ReqRes();
		Optional<Products> optional = productsRepository.findById(id);
		if (optional.isPresent()) {
			optional.get().setActive(false);
			productsRepository.save(optional.get());
			reqRes.setMessage("Xóa thành công");
			reqRes.setStatusCode(1);
			return reqRes;
		}
		reqRes.setMessage("sản phẩm không hợp lệ");
		reqRes.setStatusCode(0);
		return reqRes;
	}

	public List<ProductsDTO> getAllProduct() {
		List<ProductsDTO> dtos = new ArrayList<>();
		List<Products> list = productsRepository.findAll();
		list.forEach(p -> {
			ProductsDTO dto = new ProductsDTO();
			dto = modelMapper.map(p, ProductsDTO.class);
			dto.setCategory(modelMapper.map(p.getCategories(), CategoriesDTO.class));
			dtos.add(dto);

		});
		return dtos;
	}

	public List<ProductsDTO> getAllProductUser(PageRequest pageRequest) {
		List<ProductsDTO> dtos = new ArrayList<>();
		LocalDate date = LocalDate.now();
		List<Products> list = productsRepository.findAllProductUser(pageRequest);
		for (Products p : list) {
			double percentage = 0;

			List<ProductDiscount> discounts = productDiscountService.getDiscountByProduct(p.getId());
			List<StatisticalReviewProduct> reviewProducts = reviewsServive.satistocalReviewProduct(p.getId());

			double totalRating = 0;
			double coutRating = 0;
			for (StatisticalReviewProduct reviewProduct : reviewProducts) {

				totalRating += reviewProduct.getQuantity() * reviewProduct.getRating();
				coutRating += reviewProduct.getQuantity();
			}
			double rating;
			if (coutRating != 0)
				rating = totalRating / coutRating * 1.0;
			else
				rating = 0;
			if (!discounts.isEmpty()) {

				ProductsDTO dto = new ProductsDTO();

				dto = modelMapper.map(p, ProductsDTO.class);
				for (ProductDiscount discount : discounts) {
					if ((date.isAfter(discount.getStartDate()) && date.isBefore(discount.getEndDate()))
							|| date.isEqual(discount.getStartDate()) || date.isEqual(discount.getEndDate())) {
						percentage += discount.getDiscounts().getDiscountPercentage();
					}

				}

				dto.setPriceDiscount((long) (p.getPrice() - (p.getPrice() * percentage)));
				dto.setDiscountPercentage((int) (percentage * 100));
				dto.setCategory(modelMapper.map(p.getCategories(), CategoriesDTO.class));
				dto.setRating(rating);
				dtos.add(dto);
			} else {
				ProductsDTO dto = new ProductsDTO();
				dto = modelMapper.map(p, ProductsDTO.class);
				dto.setRating(rating);
				dto.setCategory(modelMapper.map(p.getCategories(), CategoriesDTO.class));
				dtos.add(dto);
			}

		}
		return dtos;
	}

	public List<ProductsDTO> getAllProductByCategory(String nameCategory) {
		List<ProductsDTO> dtos = new ArrayList<>();
		List<Products> list = productsRepository.findAllByCategory(nameCategory);
		list.forEach(p -> {
			List<StatisticalReviewProduct> reviewProducts = reviewsServive.satistocalReviewProduct(p.getId());

			double totalRating = 0;
			double coutRating = 0;
			for (StatisticalReviewProduct reviewProduct : reviewProducts) {

				totalRating += reviewProduct.getQuantity() * reviewProduct.getRating();
				coutRating += reviewProduct.getQuantity();
			}
			double rating;
			if (coutRating > 0)
				rating = totalRating / coutRating * 1.0;
			else
				rating = 0;
			ProductsDTO dto = new ProductsDTO();
			dto = modelMapper.map(p, ProductsDTO.class);
			dto.setRating(rating);
			dtos.add(dto);

		});
		return dtos;
	}

	public ErrorMessage createProduct(FormProduct productReq, BindingResult bindingResult) {
		ErrorMessage reqRes = new ErrorMessage();

		try {
			if (bindingResult.hasErrors()) {
				reqRes.setStatusCode(0);
				reqRes.setMessage(bindingResult.getFieldError().getDefaultMessage());
				return reqRes;
			}
			Optional<Categories> optional = categoriesRepository.findById(productReq.getCategoryId());
			if (optional.isPresent()) {
				List<Images> listIgame = new ArrayList<>();

				Products product = modelMapper.map(productReq, Products.class);
				product.setTotalQuantity(productReq.getTotalQuantity());
				product.setCategories(optional.get());
				product.setImages(listIgame);
				product.setActive(true);
				product.setCreatedDate(LocalDateTime.now());
				product.setUpdatedDate(LocalDateTime.now());
				productsRepository.save(product);

				storageService.store(productReq.getImages()).forEach(img -> {
					Images image = new Images();
					image.setImageName(img);
					image.setProducts(product);
					image.setCreatedDate(LocalDateTime.now());
					image.setUpdatedDate(LocalDateTime.now());
					image.setActive(true);
					listIgame.add(image);
					imagesRepository.save(image);
				});
				reqRes.setStatusCode(1);
				reqRes.setMessage("Thêm sản phẩm thành công");
				return reqRes;

			} else {
				reqRes.setStatusCode(0);
				reqRes.setMessage("Danh mục không hợp lệ");
			}

		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage("Error: " + e.getMessage());
		}

		return reqRes;
	}

	public ErrorMessage update(FormUpdateProduct productReq, BindingResult bindingResult) {
		ErrorMessage reqRes = new ErrorMessage();
		try {
			if (bindingResult.hasErrors()) {
				reqRes.setStatusCode(0);
				reqRes.setMessage(bindingResult.getFieldError().getDefaultMessage());
				return reqRes;
			}
			Optional<Categories> optional = categoriesRepository.findById(productReq.getCategoryId());
			Optional<Products> opProduct = productsRepository.findById(productReq.getId());
			if (opProduct.isPresent()) {

				Products products = opProduct.get();
				products.setDescription(productReq.getDescription());
				products.setCategories(optional.get());
				products.setName(productReq.getName());
				products.setPrice(productReq.getPrice());
				products.setTotalQuantity(productReq.getTotalQuantity());
				products.setUpdatedDate(LocalDateTime.now());
				if (productReq.getImages() != null) {
					List<Images> listIgame = imagesRepository.findAllByProduct(opProduct.get().getId());
					for (Images img : listIgame) {
						imagesRepository.delete(img);
						try {
							storageService.deleteFile(img.getImageName());
						} catch (FileNotFoundException e) {
							reqRes.setMessage(e.getMessage());
							reqRes.setStatusCode(0);
							return reqRes;
						}
					}
					List<Images> imagesNew = new ArrayList<>();
					storageService.store(productReq.getImages()).forEach(img -> {
						Images image = new Images();
						image.setImageName(img);
						image.setProducts(products);
						image.setActive(true);
						imagesNew.add(image);
						image.setUpdatedDate(LocalDateTime.now());
						imagesRepository.save(image);
					});
					products.setImages(imagesNew);
				}

				productsRepository.save(products);
				reqRes.setStatusCode(1);
				reqRes.setMessage("Cập nhật sản phẩm thành công");

			} else {
				reqRes.setStatusCode(0);
				reqRes.setMessage("required id category");
			}

		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage("Error: " + e.getMessage() + productReq.getName());
		}

		return reqRes;
	}

	public List<ProductsDTO> searchProducts(FormSearchProduct searchProduct, PageRequest pageRequest) {
		List<ProductsDTO> list = new ArrayList<ProductsDTO>();
		List<Products> products = productsRepository.searchProducts(searchProduct, pageRequest);

		for (Products product : products) {
			ProductsDTO dto = modelMapper.map(product, ProductsDTO.class);
			dto.setCategory(modelMapper.map(product.getCategories(), CategoriesDTO.class));
			list.add(dto);
		}

		return list;

	}

	public List<ProductsDTO> searchProductsUser(FormSearchProduct searchProduct, PageRequest pageRequest) {
		LocalDate date = LocalDate.now();
		List<ProductsDTO> list = new ArrayList<ProductsDTO>();
		List<Products> products = productsRepository.searchProductsbyUser(searchProduct, pageRequest);

		for (Products product : products) {
			List<ProductDiscount> discounts = productDiscountService.getDiscountByProduct(product.getId());
			List<StatisticalReviewProduct> reviewProducts = reviewsServive.satistocalReviewProduct(product.getId());

			double totalRating = 0;
			double coutRating = 0;
			for (StatisticalReviewProduct reviewProduct : reviewProducts) {

				totalRating += reviewProduct.getQuantity() * reviewProduct.getRating();
				coutRating += reviewProduct.getQuantity();
			}
			double rating;
			if (coutRating > 0)
				rating = totalRating / coutRating * 1.0;
			else
				rating = 0;
			double percentage = 0;
			for (ProductDiscount discount : discounts) {
				if ((date.isAfter(discount.getStartDate()) && date.isBefore(discount.getEndDate()))
						|| date.isEqual(discount.getStartDate()) || date.isEqual(discount.getEndDate())) {
					percentage += discount.getDiscounts().getDiscountPercentage();
				}
			}

			ProductsDTO dto = modelMapper.map(product, ProductsDTO.class);
			dto.setDiscountPercentage((int) (percentage * 100));
			dto.setPriceDiscount((long) (product.getPrice() - (product.getPrice() * percentage)));
			dto.setCategory(modelMapper.map(product.getCategories(), CategoriesDTO.class));
			dto.setRating(rating);
			list.add(dto);
		}

		return list;

	}

	public List<ProductsDTO> testsearchProductsUser(FormSearchProduct searchProduct, PageRequest pageRequest) {
		LocalDate date = LocalDate.now();
		List<ProductsDTO> list = new ArrayList<ProductsDTO>();
		List<Products> products = productsRepository.searchProductsUser(searchProduct, pageRequest);
		for (Products product : products) {
			List<ProductDiscount> discounts = productDiscountService.getDiscountByProduct(product.getId());
			double percentage = 0;
			for (ProductDiscount discount : discounts) {
				if ((date.isAfter(discount.getStartDate()) && date.isBefore(discount.getEndDate()))
						|| date.isEqual(discount.getStartDate()) || date.isEqual(discount.getEndDate())) {
					percentage += discount.getDiscounts().getDiscountPercentage();
				}
			}

			ProductsDTO dto = modelMapper.map(product, ProductsDTO.class);
			dto.setDiscountPercentage((int) (percentage * 100));
			dto.setPriceDiscount((long) (product.getPrice() - (product.getPrice() * percentage)));
			dto.setCategory(modelMapper.map(product.getCategories(), CategoriesDTO.class));
			list.add(dto);
		}

		return list;

	}

	public long countRepo() {
		return productsRepository.count();
	}

}
