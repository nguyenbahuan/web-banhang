package com.spring.electronicshop.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.spring.electronicshop.controller.api.user.address.FormAddress;
import com.spring.electronicshop.dto.AddressDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Address;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.repository.AddressRepository;
import com.spring.electronicshop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AddressService {

	private final AddressRepository addressRepository;

	private final UserRepository userRepository;

	private final ModelMapper modelMapper;

	private final MessageSource messageSource;

	public String message(String code) {
		return messageSource.getMessage(code, null, new Locale("vi", "VN"));
	}

	public ErrorMessage createAddress(FormAddress formAddress) {
		ErrorMessage message = new ErrorMessage();
		Optional<User> userOp = userRepository.findById(formAddress.getUserId());
		if (userOp.isPresent()) {
			Address address = modelMapper.map(formAddress, Address.class);
			address.setActive(true);
			address.setCreatedDate(LocalDateTime.now());
			address.setUpdatedDate(LocalDateTime.now());
			address.setUser(userOp.get());
			addressRepository.save(address);
			message.setMessage(messageSource.getMessage("address.create", null, new Locale("vi", "VN")));
			message.setStatusCode(1);
			return message;
		}
		message.setMessage(messageSource.getMessage("address.error", null, new Locale("vi", "VN")));
		message.setStatusCode(0);
		return message;
	}

	public Optional<Address> getAddress(Long id) {

		return addressRepository.findById(id);
	}

	public List<AddressDTO> getAllAddress(User user) {

		List<Address> list = addressRepository.findActiveAddresses(user.getId());
		List<AddressDTO> addressDTOs = list.stream().map(address -> modelMapper.map(address, AddressDTO.class))
				.toList();

		return addressDTOs;
	}

	public Object updateAddress(Long addressId, FormAddress formAddress, User user) {
		ErrorMessage message = new ErrorMessage();
		Optional<Address> optional = addressRepository.findById(addressId);
		if (optional.isPresent()) {
			optional.get().setAddress(formAddress.getAddress());
			addressRepository.save(optional.get());
			message.setMessage(message("address.update"));
			message.setStatusCode(1);
			return message;
		}
		message.setMessage(message("address.error"));
		message.setStatusCode(0);
		return message;

	}

	public Object deleteAddress(Long addressId, User user) {
		ErrorMessage message = new ErrorMessage();
		Optional<Address> optional = addressRepository.findById(addressId);
		if (optional.isPresent()) {
			addressRepository.delete(optional.get());
			message.setMessage(message("address.delete"));
			message.setStatusCode(1);
			return message;
		}
		message.setMessage(message("address.error"));
		message.setStatusCode(0);
		return message;

	}
}
