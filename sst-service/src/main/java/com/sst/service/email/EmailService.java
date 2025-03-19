package com.sst.service.email;

import static com.sst.resource.loader.ResourceLoader.loadTemplate;

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
	
	@Value("${server.url:https://sst.koyeb.app")
	private String url;

	@Autowired
	private JavaMailSender sender;
	
	public void send(String name, String email, String template, String uri) throws Exception {
        String htmlContent = loadTemplate(template);
        htmlContent = htmlContent.replace("{name}", name);
        htmlContent = htmlContent.replace("{url}", this.url + uri);
        MimeMessage mimeMessage = sender.createMimeMessage();
        mimeMessage.setHeader("X-Priority", "1");
        mimeMessage.setHeader("Importance", "high");
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(email);
        messageHelper.setPriority(1);
        messageHelper.setFrom("Verificação de Conta <sstsst@gmail.com>");
        messageHelper.setSubject("Verifique sua Conta");
        messageHelper.setText(htmlContent, true);
        log.info("SEND | Sending email to: {} - {}", email, name);
        sender.send(mimeMessage);
	}
}
