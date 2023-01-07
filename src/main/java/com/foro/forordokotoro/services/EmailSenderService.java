package com.foro.forordokotoro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail,
                                String subject,
                                String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("coulibalyadamabekaye03@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

/*
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

 */

        mailSender.send(message);
        System.out.println("Mail envoyer avec succès...");


    }

}