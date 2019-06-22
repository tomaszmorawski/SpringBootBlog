package me.spiochu.blog.Services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class AutoMailService {

    private JavaMailSender mailSender;

    public AutoMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMailTo(String to, String subject, String message) throws MessagingException {
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        mimeMailMessage.setFrom("registartion@spiochu.me");
        mimeMailMessage.setRecipients(Message.RecipientType.TO, to);
        mimeMailMessage.setSubject(subject);
        mimeMailMessage.setContent(message, "text/html; charset=utf-8");
        mailSender.send(mimeMailMessage);
    }
}
