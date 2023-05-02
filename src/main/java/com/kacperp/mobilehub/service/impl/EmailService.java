package com.kacperp.mobilehub.service.impl;

import com.kacperp.mobilehub.model.Order;
import com.kacperp.mobilehub.model.User;
import com.kacperp.mobilehub.service.IEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService implements IEmailService {
    @Value("${spring.mail.username}")
    private String emailAddress;
    @Value("${spring.mail.password}")
    private String emailPassword;
    @Value("${spring.mail.host}")
    private String host;

    @Override
    public void sendOrderConfirmationEmail(User user, Order order) {
        String recipientEmailAddress = user.getEmail();
        String recipientName = user.getFirstname();
        Long orderId = order.getId();
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, emailPassword);
            }
        });
        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmailAddress));
            message.setSubject("[MobileHub] Order Confirmation");
            message.setText("Dear " + recipientName + ",\n\nYour order " + orderId +
                    " has been received and is being processed.\n\nThank you for your business." +
                    "\n\nBest regards\nMobileHub");
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
