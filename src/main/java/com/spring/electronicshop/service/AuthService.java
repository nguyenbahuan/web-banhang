package com.spring.electronicshop.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.spring.electronicshop.dto.ReqRes;
import com.spring.electronicshop.jwt.JWTUtils;
import com.spring.electronicshop.model.Login;
import com.spring.electronicshop.model.User;
import com.spring.electronicshop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final UserRepository userRepository;
	private final JWTUtils jwtUtils;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final MessageSource messageSource;

	public String message(String code) {
		return messageSource.getMessage(code, null, new Locale("vi", "VN"));
	}

	public ReqRes signUp(ReqRes registrationRequest, BindingResult bindingResult) {
		ReqRes resp = new ReqRes();
		Optional<User> optionalUser = userRepository.findUserByEmail(registrationRequest.getEmail());
		try {
			if (bindingResult.hasErrors()) {
				resp.setMessage(bindingResult.getFieldError().getDefaultMessage());
				resp.setStatusCode(200);
				return resp;
			}
			if (optionalUser.isPresent()) {
				resp.setMessage(message("email.exist"));
				resp.setStatusCode(200);
				return resp;
			} else {
				User ourUsers = new User();
				ourUsers.setEmail(registrationRequest.getEmail());
				ourUsers.setName(registrationRequest.getName());
				ourUsers.setDob(registrationRequest.getDob());
				ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
				User ourUserResult = userRepository.save(ourUsers);
				if (ourUserResult != null && ourUserResult.getId() > 0) {
					resp.setMessage(message("register.succses"));
					resp.setStatusCode(200);
				}
			}

		} catch (Exception e) {
			resp.setStatusCode(500);
			resp.setError("error register " + e.getMessage());

		}
		return resp;
	}

	public Login signIn(Login signinRequest, BindingResult bindingResult) throws Exception {
		Login response = new Login();

		try {
			if (bindingResult.hasErrors()) {
				response.setError(bindingResult.getFieldError().getDefaultMessage());
				response.setStatusCode(200);
			} else {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
				var user = userRepository.findUserByEmail(signinRequest.getEmail()).orElseThrow();
				var jwt = jwtUtils.generateToken(user);
				var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
				response.setStatusCode(200);
				response.setToken(jwt);
				response.setRefreshToken(refreshToken);
				response.setExpirationTime("24Hr");
				response.setMessage(message("login.succses"));
			}

		} catch (AuthenticationException e) {

			response.setStatusCode(200);
			response.setError(message("login.fail"));
		} catch (NoSuchElementException e) {

			response.setStatusCode(201);
			response.setError(message("login.fail"));
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setError(message("login.fail"));

		}
		return response;
	}

	public ReqRes refreshToken(ReqRes refreshTokenReqiest) {
		ReqRes response = new ReqRes();
		String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
		User users = userRepository.findUserByEmail(ourEmail).orElseThrow();
		if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
			var jwt = jwtUtils.generateToken(users);
			response.setStatusCode(200);
			response.setToken(jwt);
			response.setRefreshToken(refreshTokenReqiest.getToken());
			response.setExpirationTime("24Hr");
			response.setMessage("Successfully Refreshed Token");
		}
		response.setStatusCode(500);
		response.setMessage(ourEmail + users.getName());
		return response;
	}
}
