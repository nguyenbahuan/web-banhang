package com.spring.electronicshop.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.spring.electronicshop.controller.api.admin.auth.FormLogin;
import com.spring.electronicshop.controller.api.admin.auth.FormRegister;
import com.spring.electronicshop.controller.api.admin.staff.FormStaff;
import com.spring.electronicshop.controller.api.user.auth.FormUserLogin;
import com.spring.electronicshop.controller.api.user.auth.FormUserRegister;
import com.spring.electronicshop.dto.RefreshTokenDTO;
import com.spring.electronicshop.jwt.JWTUtils;
import com.spring.electronicshop.jwt.JwtUser;
import com.spring.electronicshop.message.ErrorMessage;
import com.spring.electronicshop.model.Role;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.repository.RoleRepository;
import com.spring.electronicshop.repository.UserRepository;
import com.spring.electronicshop.storage.StorageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginService {

	private final RoleRepository roleRepository;
	private final StorageService storageService;
	private final UserRepository userRepository;
	private final JWTUtils jwtUtils;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final ModelMapper modelMapper;
	private final MessageSource messageSource;

	public JwtUser signInAdmin(FormLogin signinRequest, BindingResult bindingResult) throws Exception {

		JwtUser response = new JwtUser();
		try {
			if (bindingResult.hasErrors()) {
				response.setError(bindingResult.getFieldError().getDefaultMessage());
				response.setStatusCode(0);
			} else {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
				var user = userRepository.findUserByEmail(signinRequest.getEmail()).orElseThrow();

				if (user.getRole().getName().equals("USER") || user == null) {
					response.setError(messageSource.getMessage("login.fail", null, new Locale("vi", "VN")));
					response.setStatusCode(0);
					return response;
				}

				var jwt = jwtUtils.generateToken(user);
//				var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
				response.setStatusCode(1);
				response.setTokenAccess(jwt);
				response.setEmail(user.getEmail());
				response.setName(user.getName());
				response.setDob(user.getDob());
				response.setPhoneNumber(user.getPhoneNumber());
				response.setMessage("Đăng nhập thành công");
			}

		} catch (AuthenticationException e) {
			response.setStatusCode(200);
			response.setError(messageSource.getMessage("login.fail", null, new Locale("vi", "VN")));
		} catch (NoSuchElementException e) {
			response.setStatusCode(200);
			response.setError(messageSource.getMessage("login.fail", null, new Locale("vi", "VN")));
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setError(messageSource.getMessage("login.fail", null, new Locale("vi", "VN")));

		}
		return response;
	}

	public ErrorMessage register(@Valid FormRegister request, BindingResult bindingResult) {
		ErrorMessage message = new ErrorMessage();
		Optional<User> optionalUser = userRepository.findUserByEmail(request.getEmail());
		try {
			if (bindingResult.hasErrors()) {
				message.setMessage(bindingResult.getFieldError().getDefaultMessage());
				message.setStatusCode(0);
				return message;
			}
			if (!request.getConfirmPassword().equals(request.getPassword())) {
				message.setMessage(messageSource.getMessage("pass.confirm", null, new Locale("vi", "VN")));
				message.setStatusCode(0);
				return message;
			}
			if (optionalUser.isPresent()) {
				message.setMessage(messageSource.getMessage("email.exist", null, new Locale("vi", "VN")));
				message.setStatusCode(0);
				return message;
			} else {
				Optional<Role> role = roleRepository.findById(request.getRoleId());
				if (role.isPresent()) {
					User ok = modelMapper.map(request, User.class);
					ok.setRole(role.get());
					ok.setPassword(passwordEncoder.encode(request.getPassword()));
					ok.setRole(role.get());
					ok.setActive(true);
					ok.setCreatedDate(LocalDateTime.now());
					User ourUserResult = userRepository.save(ok);
					if (ourUserResult != null && ourUserResult.getId() > 0) {
						message.setMessage(messageSource.getMessage("register.succses", null, new Locale("vi", "VN")));
						message.setStatusCode(1);
						return message;
					}
				}
				message.setMessage("not found role");
				message.setStatusCode(0);

			}

		} catch (Exception e) {
			message.setStatusCode(500);
			message.setMessage("ok error register " + e.getMessage());

		}
		return message;
	}

	public JwtUser signInUser(FormUserLogin loginRequest, BindingResult bindingResult) {
		JwtUser response = new JwtUser();
		try {
			if (bindingResult.hasErrors()) {
				response.setError(bindingResult.getFieldError().getDefaultMessage());
				response.setStatusCode(0);
			} else {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
				var user = userRepository.findUserByEmail(loginRequest.getEmail()).orElseThrow();

				if (!user.getRole().getName().equals("USER") || user == null) {
					response.setError(messageSource.getMessage("login.fail", null, new Locale("vi", "VN")));
					response.setStatusCode(0);
					return response;
				}
				var jwt = jwtUtils.generateToken(user);
//				var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
				response.setAvatar(user.getAvatar());
				response.setDob(user.getDob());
				response.setName(user.getName());
				response.setEmail(user.getEmail());
				response.setPhoneNumber(user.getPhoneNumber());
				response.setStatusCode(1);
				response.setTokenAccess(jwt);
				response.setMessage(messageSource.getMessage("login.succses", null, new Locale("vi", "VN")));
			}

		} catch (AuthenticationException e) {
			response.setStatusCode(200);
			response.setError(messageSource.getMessage("login.fail", null, new Locale("vi", "VN")));
		} catch (NoSuchElementException e) {
			response.setStatusCode(200);
			response.setError(messageSource.getMessage("login.fail", null, new Locale("vi", "VN")));
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setError(messageSource.getMessage("login.fail", null, new Locale("vi", "VN")));

		}

		return response;
	}

	public ErrorMessage registerUser(FormUserRegister request, BindingResult bindingResult) {
		ErrorMessage message = new ErrorMessage();
		Optional<User> optionalUser = userRepository.findUserByEmail(request.getEmail());
		try {
			if (bindingResult.hasErrors()) {
				message.setMessage(bindingResult.getFieldError().getDefaultMessage());
				message.setStatusCode(0);
				return message;
			}
			if (!request.getConfirmPassword().equals(request.getPassword())) {
				message.setMessage(messageSource.getMessage("pass.confirm", null, new Locale("vi", "VN")));
				message.setStatusCode(0);
				return message;
			}
			if (optionalUser.isPresent()) {
				message.setMessage(messageSource.getMessage("email.exist", null, new Locale("vi", "VN")));
				message.setStatusCode(0);
				return message;
			} else {
				Optional<Role> role = roleRepository.findById((long) 2);
				if (role.isPresent() && role.get().getName().equals("USER")) {
					User user = modelMapper.map(request, User.class);
					user.setRole(role.get());
					user.setPassword(passwordEncoder.encode(request.getPassword()));
					user.setRole(role.get());
					user.setActive(true);
					user.setCreatedDate(LocalDateTime.now());
					user.setUpdatedDate(LocalDateTime.now());
					User ourUserResult = userRepository.save(user);
					if (ourUserResult != null && ourUserResult.getId() > 0) {
						message.setMessage(messageSource.getMessage("register.succses", null, new Locale("vi", "VN")));
						message.setStatusCode(1);
						return message;
					}
				}
				message.setMessage("vai trò không phù hợp");
				message.setStatusCode(0);

			}

		} catch (Exception e) {
			message.setStatusCode(500);
			message.setMessage("ok error register " + e.getMessage());

		}
		return message;
	}

	public ErrorMessage registerStaff(FormStaff request, BindingResult bindingResult) {

		ErrorMessage message = new ErrorMessage();
		Optional<User> optionalUser = userRepository.findUserByEmail(request.getEmail());
		try {
			if (bindingResult.hasErrors()) {
				message.setMessage("hasErrors " + bindingResult.getFieldError().getDefaultMessage());
				message.setStatusCode(0);
				return message;
			}

			if (optionalUser.isPresent()) {
				message.setMessage(messageSource.getMessage("email.exist", null, new Locale("vi", "VN")));
				message.setStatusCode(0);
				return message;
			} else {
				Optional<Role> role = roleRepository.findById(request.getRoleId());
				if (role.isPresent()) {
					User user = modelMapper.map(request, User.class);
					user.setPassword(passwordEncoder.encode(request.getPassword()));
					user.setRole(role.get());
					user.setActive(true);
					user.setCreatedDate(LocalDateTime.now());
					user.setUpdatedDate(LocalDateTime.now());
					storageService.store(request.getAvatar()).forEach(img -> {
						user.setAvatar(img);
					});

					User ourUserResult = userRepository.save(user);
					if (ourUserResult != null && ourUserResult.getId() > 0) {
						message.setMessage(messageSource.getMessage("register.staff", null, new Locale("vi", "VN")));
						message.setStatusCode(1);
						return message;
					}
				}
				message.setMessage("không tìm thấy vai trò hợp lệ");
				message.setStatusCode(0);

			}

		} catch (Exception e) {
			message.setStatusCode(500);
			message.setMessage("ok error register " + e.getMessage());

		}
		return message;
	}

	public RefreshTokenDTO refreshToken(RefreshTokenDTO token, User user) {
		var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
		RefreshTokenDTO tokenDTO = new RefreshTokenDTO();
		tokenDTO.setToken(refreshToken);
		return tokenDTO;

	}

}
