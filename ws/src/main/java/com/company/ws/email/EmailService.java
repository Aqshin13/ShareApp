package com.company.ws.email;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Value("app.email.username")
    private String emailUsername;
    @Value("app.email.password")
    private String emailPassword;

    private JavaMailSenderImpl mailSender;
    private String activationEmail = """
            <html>
                <body>
                    <h1>${title}</h1>
                    <a href="${url}">Active the account</a>
                </body>
            </html>
            """;

    @PostConstruct
    public void sender() {
        this.mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.ethereal.email");
        mailSender.setPort(587);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", "true");
    }


    public void sendActivationToken(String email, String activationToken) {
        var activationUrl = "http://localhost:5173/activation/" + activationToken;
        String mailBody = activationEmail
                .replace("${title}", "Activate your account")
                .replace("${url}", activationUrl);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            message.setFrom("socialapp@mail.ru");
            message.setTo(email);
            message.setSubject("Activate your account");
            message.setText(mailBody, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        this.mailSender.send(mimeMessage);

    }


}
