package com.spring.electronicshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.spring.electronicshop.contants.OderStatus;
import com.spring.electronicshop.controller.api.admin.oder.SearchOder;
import com.spring.electronicshop.controller.api.admin.oder.UpdateOder;
import com.spring.electronicshop.controller.api.user.oder.FormOder;
import com.spring.electronicshop.controller.api.user.oder.OderDetailShow;
import com.spring.electronicshop.dto.CategoriesDTO;
import com.spring.electronicshop.dto.OderDetailsDTO;
import com.spring.electronicshop.dto.OdersDTO;
import com.spring.electronicshop.dto.ProductsDTO;
import com.spring.electronicshop.dto.UserDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Address;
import com.spring.electronicshop.model.Oders;
import com.spring.electronicshop.model.OdersDetails;
import com.spring.electronicshop.model.Products;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.repository.AddressRepository;
import com.spring.electronicshop.repository.OdersRepository;
import com.spring.electronicshop.repository.ProductsRepository;
import com.spring.electronicshop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OdersService {

	private final ProductsRepository productsRepository;
	private final OdersRepository odersRepository;
	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final EmailService emailService;
	private final ModelMapper modelMapper;

	public Long oderCount() {
		return odersRepository.count();
	}

	public Long oderCountByUser(User user) {
		return odersRepository.countByUser(user.getId());
	}

	public List<OdersDTO> getAllOder(PageRequest pageRequest, User user) {
		List<Oders> list = odersRepository.findByUser(pageRequest, user);

		List<OdersDTO> dtos = new ArrayList<>();

		// list oder
		for (Oders oder : list) {
			OdersDTO odersDTO = modelMapper.map(oder, OdersDTO.class);
			odersDTO.setAddress(oder.getAddress().getAddress());
			List<OderDetailsDTO> detailsDTO = new ArrayList<>();
			for (OdersDetails oderdetai : oder.getOdersDetails()) {
				OderDetailsDTO detailDTO = new OderDetailsDTO();
				detailDTO.setProductId(oderdetai.getProducts().getId());
				detailDTO.setQuantity(oderdetai.getQuantity());
				detailDTO.setSubPrice(oderdetai.getSubPrice());
				odersDTO.setAddress(oder.getAddress().getAddress());
				detailsDTO.add(detailDTO);
			}

			odersDTO.setTotal(oder.getTotal());
			odersDTO.setOderDetails(detailsDTO);
			odersDTO.setUserId(user.getId());
			odersDTO.setUsername(user.getName());

			dtos.add(odersDTO);
		}

		return dtos;
	}

	public List<OdersDTO> getAllOrder(User user) {
		List<Oders> list = odersRepository.findOrderByUser(user.getId());

		List<OdersDTO> dtos = new ArrayList<>();

		// list oder
		for (Oders oder : list) {
			OdersDTO odersDTO = modelMapper.map(oder, OdersDTO.class);
			odersDTO.setAddress(oder.getAddress().getAddress());
			List<OderDetailsDTO> detailsDTO = new ArrayList<>();
			for (OdersDetails oderdetai : oder.getOdersDetails()) {
				OderDetailsDTO detailDTO = new OderDetailsDTO();
				detailDTO.setProductId(oderdetai.getProducts().getId());
				detailDTO.setQuantity(oderdetai.getQuantity());
				detailDTO.setSubPrice(oderdetai.getSubPrice());
				odersDTO.setAddress(oder.getAddress().getAddress());
				detailsDTO.add(detailDTO);
			}
			odersDTO.setTotal(oder.getTotal());
			odersDTO.setOderDetails(detailsDTO);
			odersDTO.setUserId(user.getId());
			odersDTO.setUsername(user.getName());

			dtos.add(odersDTO);
		}

		return dtos;
	}

	public List<OdersDTO> getAll(SearchOder searchOder, PageRequest pageRequest) {

		List<Oders> list = odersRepository.findAll(pageRequest).getContent();
		List<OdersDTO> dtos = new ArrayList<>();

		for (Oders oder : list) {
			OdersDTO odersDTO = modelMapper.map(oder, OdersDTO.class);
			odersDTO.setUserId(oder.getUser().getId());
			odersDTO.setUsername(oder.getUser().getName());
			odersDTO.setPhoneNumber(oder.getUser().getPhoneNumber());
			odersDTO.setAddress(oder.getAddress().getAddress());
			odersDTO.setTotal(oder.getTotal());
			List<OderDetailsDTO> detailsDTO = new ArrayList<>();

			for (OdersDetails oderdetai : oder.getOdersDetails()) {
				OderDetailsDTO detailDTO = new OderDetailsDTO();
				detailDTO.setProductId(oderdetai.getProducts().getId());
				detailDTO.setQuantity(oderdetai.getQuantity());
				detailsDTO.add(detailDTO);
			}
			odersDTO.setOderDetails(detailsDTO);
			dtos.add(odersDTO);
		}

		return dtos;
	}

	public ErrorMessage createOder(FormOder formOder) {
		ErrorMessage message = new ErrorMessage();
		Optional<User> user = userRepository.findById(formOder.getUserId());
		Optional<Address> addressOp = addressRepository.findById(formOder.getAddressId());
		if (user.isPresent()) {
			if (addressOp.isPresent()) {
				List<Address> addresses = addressRepository.findByUser(user.get());
				if (!addresses.contains(addressOp.get())) {
					message.setMessage("dia chi khong hop le");
					message.setStatusCode(0);
					return message;
				}

				List<OdersDetails> odersDetails = new ArrayList<>();
				OdersDTO oder = modelMapper.map(formOder, OdersDTO.class);
				List<OderDetailsDTO> detailsDTO = formOder.getOderDetails().stream()
						.map(od -> modelMapper.map(od, OderDetailsDTO.class)).toList();
				oder.setOderDetails(detailsDTO);
				Oders oders = modelMapper.map(oder, Oders.class);
				Long total = (long) 0;
				for (OderDetailsDTO item : oder.getOderDetails()) {
					OdersDetails details = new OdersDetails();
					Optional<Products> product = productsRepository.findById(item.getProductId());
					if (product.isPresent()) {
						if (product.get().getTotalQuantity() < item.getQuantity()) {
							message.setMessage("so luong hang khong du");
							message.setStatusCode(0);
							return message;

						}
						total += item.getQuantity() * item.getSubPrice();
						details.setOders(oders);
						details.setProducts(product.get());
						details.setQuantity(item.getQuantity());
						details.setSubPrice(item.getSubPrice());
						odersDetails.add(details);
					} else {
						message.setMessage("san pham khong ton tai");
						message.setStatusCode(0);
						return message;
					}

				}
				oders.setTotal(total);
				oders.setStatus(0);
				oders.setUser(user.get());
				oders.setAddress(addressOp.get());
				oders.setOdersDetails(odersDetails);

				odersRepository.save(oders);
				message.setMessage("đặt hàng thành công");
				message.setStatusCode(1);
				return message;
			} else {
				message.setMessage("dia chi khong hop le");
				message.setStatusCode(0);
				return message;
			}

		}
		message.setMessage("khong thay user");
		message.setStatusCode(0);
		return message;

//		odersRepository.save(oder);

	}

	public OdersDTO getOder(Long id) {
		// TODO Auto-generated method stub
		Optional<Oders> optional = odersRepository.findById(id);
		List<OderDetailsDTO> detailsDTO = new ArrayList<>();
		for (OdersDetails oder : optional.get().getOdersDetails()) {
			OderDetailsDTO detailDTO = new OderDetailsDTO();
			detailDTO.setProductId(oder.getProducts().getId());
			detailDTO.setQuantity(oder.getQuantity());
			detailsDTO.add(detailDTO);
		}
//		optional.get().getOdersDetails().forEach(oder -> {
//			OderDetailsDTO detailDTO = new OderDetailsDTO();
//			detailDTO.setProductId(oder.getProducts().getId());
//			detailDTO.setQuantity(oder.getQuantity());
//			detailsDTO.add(detailDTO);
//		});
		OdersDTO odersDTO = modelMapper.map(optional.get(), OdersDTO.class);
		odersDTO.setAddress(optional.get().getAddress().getAddress());

		odersDTO.setUserId(optional.get().getUser().getId());
		odersDTO.setOderDetails(detailsDTO);
		odersDTO.setUsername(optional.get().getUser().getName());
		odersDTO.setPhoneNumber(optional.get().getUser().getPhoneNumber());
		return odersDTO;

	}

	public List<OderDetailShow> getOderDetail(User user, Long id) {
		Optional<Oders> optional = odersRepository.findById(id);

		List<OdersDetails> details = optional.get().getOdersDetails();
		List<OderDetailShow> detailsDTOs = new ArrayList<>();
		details.forEach(dt -> {
			OderDetailShow detailShow = new OderDetailShow();
			ProductsDTO productsDTO = modelMapper.map(dt.getProducts(), ProductsDTO.class);
			productsDTO.setCategory(modelMapper.map(dt.getProducts().getCategories(), CategoriesDTO.class));
			detailShow.setProducts(productsDTO);
			detailShow.setQuantity(dt.getQuantity());
			detailShow.setSubPrice(dt.getSubPrice());
			detailsDTOs.add(detailShow);
		});
		return detailsDTOs;
	}

	public ErrorMessage changeStatus(Long id, UpdateOder updateOder) {
		ErrorMessage message = new ErrorMessage();
		Optional<Oders> odersOp = odersRepository.findById(id);

		if (updateOder.getNote().isEmpty()) {
			message.setMessage("Ghi chú không được rỗng");
			message.setStatusCode(0);
			return message;
		}
		if (odersOp.isPresent()) {
			Oders oders = odersOp.get();
			UserDTO userDTO = modelMapper.map(oders.getUser(), UserDTO.class);
			int statusOld = oders.getStatus();
			if (statusOld == OderStatus.CANCEL.getCode()) {
				message.setStatusCode(1);
				message.setMessage("Đơn hàng đã bị hủy");
				return message;
			}
			if (statusOld == OderStatus.PENDING.getCode() && updateOder.getStatus() == OderStatus.APPROVED.getCode()) {

				emailService.sendMailNotifyOrder(userDTO, updateOder.getNote(), oders.getId());

				oders.setStatus(updateOder.getStatus());
				odersRepository.save(oders);
				message.setStatusCode(1);
				message.setMessage("Đơn hàng đã được tiếp nhận");
				return message;
			}
			if (statusOld == OderStatus.PENDING.getCode() && updateOder.getStatus() == OderStatus.REJECTED.getCode()) {

				emailService.sendMailNotifyOrder(userDTO, updateOder.getNote(), oders.getId());

				oders.setStatus(updateOder.getStatus());
				odersRepository.save(oders);
				message.setStatusCode(1);
				message.setMessage("Đơn hàng không được tiếp nhận");
				return message;
			}
			if (statusOld == OderStatus.APPROVED.getCode()
					&& updateOder.getStatus() == OderStatus.DELIVERING.getCode()) {

				emailService.sendMailNotifyOrder(userDTO, updateOder.getNote(), oders.getId());

				oders.setStatus(updateOder.getStatus());
				odersRepository.save(oders);
				message.setStatusCode(1);
				message.setMessage("Đơn hàng đang được giao");
				return message;
			}
			if (statusOld == OderStatus.DELIVERING.getCode() && updateOder.getStatus() == OderStatus.CANCEL.getCode()) {
				message.setStatusCode(0);
				message.setMessage("Đơn hàng không thể huỷ");
				return message;
			}
			if ((statusOld == OderStatus.PENDING.getCode() || statusOld == OderStatus.APPROVED.getCode())
					&& updateOder.getStatus() == OderStatus.CANCEL.getCode()) {
				emailService.sendMailNotifyOrder(userDTO, updateOder.getNote(), oders.getId());

				oders.setStatus(updateOder.getStatus());
				odersRepository.save(oders);
				message.setStatusCode(1);
				message.setMessage("Đơn hàng đã bị hủy");
				return message;
			}
			if (statusOld == OderStatus.DELIVERING.getCode()
					&& updateOder.getStatus() == OderStatus.COMPLETED.getCode()) {

				emailService.sendMailNotifyOrder(userDTO, updateOder.getNote(), oders.getId());

				oders.setStatus(updateOder.getStatus());
				odersRepository.save(oders);
				for (OdersDetails details : oders.getOdersDetails()) {
					Products products = details.getProducts();
					products.setTotalQuantity(products.getTotalQuantity() - details.getQuantity());
					productsRepository.save(products);

				}
				message.setStatusCode(1);
				message.setMessage("don hang da duoc giao");
				return message;
			}
		} else {
			message.setMessage("khong tim thay don hang");
			message.setStatusCode(0);
			return message;
		}
		return message;

	}

	public List<OdersDTO> getOderHistory(User user) {
		List<Oders> list = odersRepository.findOderHistory(user.getId());
		List<OdersDTO> dtos = new ArrayList<>();
		for (Oders oder : list) {
			OdersDTO odersDTO = modelMapper.map(oder, OdersDTO.class);
			odersDTO.setAddress(oder.getAddress().getAddress());
			List<OderDetailsDTO> detailsDTO = new ArrayList<>();
			for (OdersDetails oderdetai : oder.getOdersDetails()) {
				OderDetailsDTO detailDTO = new OderDetailsDTO();
				detailDTO.setProductId(oderdetai.getProducts().getId());
				detailDTO.setQuantity(oderdetai.getQuantity());
				detailDTO.setSubPrice(oderdetai.getSubPrice());
				odersDTO.setAddress(oder.getAddress().getAddress());
				detailsDTO.add(detailDTO);
			}
			odersDTO.setTotal(oder.getTotal());
			odersDTO.setOderDetails(detailsDTO);
			odersDTO.setUserId(user.getId());
			odersDTO.setUsername(user.getName());

			dtos.add(odersDTO);
		}

		return dtos;
	}

	public ErrorMessage changeStatusUser(Long id, UpdateOder updateOder) {
		ErrorMessage message = new ErrorMessage();
		Optional<Oders> odersOp = odersRepository.findById(id);
		if (odersOp.isPresent()) {
			Oders oders = odersOp.get();
			int statusOld = oders.getStatus();

			if (statusOld == OderStatus.DELIVERING.getCode()
					&& updateOder.getStatus() == OderStatus.COMPLETED.getCode()) {
				oders.setStatus(updateOder.getStatus());
				odersRepository.save(oders);
				for (OdersDetails details : oders.getOdersDetails()) {
					Products products = details.getProducts();
					products.setTotalQuantity(products.getTotalQuantity() - details.getQuantity());
					productsRepository.save(products);

				}
				message.setStatusCode(1);
				message.setMessage("Đơn hàng được tiếp nhận");
				return message;
			}
		} else {
			message.setMessage("khong tim thay don hang");
			message.setStatusCode(0);
			return message;
		}
		return message;
	}

}
