package com.spring.electronicshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.electronicshop.controller.api.admin.staff.FormSearchStaff;
import com.spring.electronicshop.controller.api.admin.staff.FormStaff;
import com.spring.electronicshop.dto.RoleDTO;
import com.spring.electronicshop.dto.UserDTO;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Role;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.repository.RoleRepository;
import com.spring.electronicshop.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final ModelMapper modelMapper;

	// tao nhan vien
	public ErrorMessage createStaff(FormStaff formStaff) {
		ErrorMessage message = new ErrorMessage();
		Optional<Role> roleOp = roleRepository.findById(formStaff.getRoleId());
		Optional<User> userOp = userRepository.findUserByEmail(formStaff.getEmail());
		if (userOp.isPresent()) {
			message.setMessage("email da ton tai");
			return message;
		}
		if (roleOp.isEmpty()) {
			message.setMessage("role khong ton tai");
			return message;
		}
		User user = modelMapper.map(formStaff, User.class);
		user.setRole(roleOp.get());
		userRepository.save(user);

		return message;
	}

	public List<UserDTO> getAllStaff(FormSearchStaff formSearchStaff, PageRequest pageRequest) {

		List<User> list = userRepository.findAllStaff(pageRequest).getContent();
		List<UserDTO> userDTOs = list.stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();

		return userDTOs;
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}

	public Optional<UserDTO> getProfile(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			UserDTO userDTO = modelMapper.map(user, UserDTO.class);

			return Optional.of(userDTO);
		} else {

			return Optional.of(null);
		}

	}

	public Optional<User> findUserbyEmail(String email) {
		Optional<User> user = userRepository.findUserByEmail(email);
		if (user.isPresent()) {
			return user;
		} else {

			return user;
		}

	}

	public UserDTO findUserbyId(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			UserDTO dto = modelMapper.map(user.get(), UserDTO.class);
			return dto;
		}
		return null;

	}

	public User loadUserByEmail(String email) {
		Optional<User> user = userRepository.findUserByEmail(email);
		if (user.isPresent()) {
			return user.get();
		} else {

			throw new IllegalStateException("email not find");
		}

	}

	public Optional<User> loadUserById(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		return user;

	}

	public List<UserDTO> getUsers() {
		List<UserDTO> list = new ArrayList<>();
		List<User> users = userRepository.findAll();
		users.forEach(u -> {
			UserDTO user = modelMapper.map(u, UserDTO.class);
			user.setRole(modelMapper.map(u.getRole(), RoleDTO.class));
			list.add(user);
		});
//		 modelMapper.
		return list;
	}

	public List<UserDTO> getAllCustomer(PageRequest pageRequest) {
		List<UserDTO> list = new ArrayList<>();
		List<User> users = userRepository.findAllCustomer(pageRequest).getContent();
		users.forEach(u -> {
			UserDTO user = modelMapper.map(u, UserDTO.class);
			user.setRole(modelMapper.map(u.getRole(), RoleDTO.class));
			list.add(user);
		});
//		 modelMapper.
		return list;
	}

	public Long countCustomer() {
		Long count = userRepository.countByCustomers();
		return count;

	}

	public void addNewUser(User user) {
		Optional<User> optionalUser = userRepository.findUserByEmail(user.getEmail());
		if (optionalUser.isPresent()) {
			throw new IllegalStateException("email taken");
		} else {
//			String pass = this.passwordEncoder.encode(user.getPassword());
//			user.setPassword(pass);
			userRepository.save(user);
		}

	}

	public ErrorMessage deleteUser(Long userId) {
		ErrorMessage message = new ErrorMessage();
		Optional<User> optional = userRepository.findById(userId);
		if (optional.isEmpty()) {
			message.setMessage("Xóa người dùng thất bại");
			return message;
		}
		message.setStatusCode(1);
		message.setMessage("Xóa người dùng thành công");
		optional.get().setActive(false);
		userRepository.save(optional.get());
		return message;
	}

	@Transactional
	public void updateUser(Long userId, String name, String email) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalStateException("user " + userId + " don't exists"));

		if (name != null && name.length() > 0 && !user.getName().equals(name)) {
			user.setName(name);
		}
		if (email != null && email.length() > 0 && !user.getEmail().equals(email)) {
			user.setEmail(email);
		}

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findUserByEmail(username);
		return user.orElseThrow();

	}

	public void changePassword(User user, String newPassword) {

		user.setPassword(newPassword);
		userRepository.save(user);

	}

	public void updateProfile(User user) {
		userRepository.save(user);

	}

}
