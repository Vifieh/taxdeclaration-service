package com.example.taxdeclarationservice.service.serviceImpl;

import com.example.taxdeclarationservice.exception.BadRequestException;
import com.example.taxdeclarationservice.model.EmailDetails;
import com.example.taxdeclarationservice.model.User;
import com.example.taxdeclarationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;


    @Override
    public void sendUserRegistration(User user, EmailDetails email, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Context context = new Context();
            context.setVariable("email", user.getEmail());
            context.setVariable("name", user.getEmail().substring(0, user.getEmail().indexOf("@")));
            context.setVariable("link", link);
            helper.setFrom(email.getFrom());
            helper.setTo(user.getEmail());
            helper.setSubject(email.getSubjectConfirmAccount());
            String html = templateEngine.process(email.getRegistrationTemplate(), context);
            helper.setText(html, true);

            log.info("Sending email: {} with html body: {}", email, html);
            mailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new BadRequestException("failed to send email");
        }
    }
}
