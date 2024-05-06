package com.spring.electronicshop.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.spring.electronicshop.dto.UserDTO;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailService {

	private final JavaMailSender javaMailSender;

	private final TemplateEngine templateEngine;
//	@Autowired
//	ThymeleafService thymeleafService;

	@Value("${spring.mail.username}")
	private String email;

	@Async("taskExecutor")
	public void sendMail(String toEmail, String subject, Map<String, Object> variables, String templatePath) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariables(variables);
			helper.setTo(toEmail);
			helper.setSubject(subject);
			String content = templateEngine.process(templatePath, context);
			helper.setText(content, true);
			helper.setFrom(email);
			javaMailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Async
	public void sendMailNotifyOrder(UserDTO userDTO, String note, Long orderId) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("note", note);
		variables.put("customerUserName", userDTO.getName());
		variables.put("orderId", String.format("%05d", orderId));
		sendMail(userDTO.getEmail(), "Thông Báo Đơn Đặt Hàng", variables, "oder-mail-template");
	}
}
