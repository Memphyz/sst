package com.healthrib.service.email;

import static com.healthrib.resource.loader.ResourceLoader.loadTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {
	
	@Value("${server.url}")
	private String url;

	@Autowired
	private JavaMailSender sender;
	
	public void send(String name, String email, String token) throws Exception {
        String htmlContent = loadTemplate("email.html");
        htmlContent = htmlContent.replace("{name}", name);
        htmlContent = htmlContent.replace("{url}", this.url + "/authorization/verify/" + email + "/" + token);
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
