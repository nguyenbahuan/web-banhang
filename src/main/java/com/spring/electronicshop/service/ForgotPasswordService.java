package com.spring.electronicshop.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.spring.electronicshop.model.ForgotPassword;
import com.spring.electronicshop.repository.ForgotPasswordRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

	private final ForgotPasswordRepository forgotPasswordRepository;

	private final JavaMailSender javaMailSender;

	private final TemplateEngine templateEngine;

	private final int MINUTES = 5;

	@Value("${spring.mail.username}")
	private String email;

	public String generateToken() {
		return UUID.randomUUID().toString();
	}

	public LocalDateTime expireTime() {
		return LocalDateTime.now().plusMinutes(MINUTES);
	}

	@Async("taskExecutor")
	public void sendEmail(String to, String subject, String emaiLink)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		Context context = new Context();
		Map<String, Object> variables = new HashMap<>();
		variables.put("emailLink", emaiLink);
		context.setVariables(variables);

		String content = templateEngine.process("mail-forgot-password", context);
		context.setVariables(variables);
		helper.setText(content, true);
		helper.setFrom(email, "admin support");
		helper.setSubject(subject);
		helper.setTo(to);
		javaMailSender.send(message);
	}

	public Optional<ForgotPassword> getToken(String token) {
		return forgotPasswordRepository.findByToken(token);
	}

	public void saveToken(ForgotPassword forgotPassword) {
		forgotPasswordRepository.save(forgotPassword);
	}
}
