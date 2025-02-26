package com.healthrib.service.email;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

	@Autowired
	private JavaMailSender sender;
	
	public void send(String name, String email) throws Exception {
        String htmlContent = new String(Files.readAllBytes(Paths.get("src/main/resources/email-template/template.html")));
        htmlContent = htmlContent.replace("{name}", name);
        MimeMessage mimeMessage = sender.createMimeMessage();
        mimeMessage.setHeader("X-Priority", "1");
        mimeMessage.setHeader("Importance", "high");
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(email);
        messageHelper.setPriority(1);
        messageHelper.setFrom("Verificação de Conta <healthribsst@gmail.com>");
        messageHelper.setSubject("Verifique sua Conta");
        messageHelper.setText(htmlContent, true);
        log.info("SEND | Sending verification email to: {} - {}", email, name);
        sender.send(mimeMessage);
	}
}
