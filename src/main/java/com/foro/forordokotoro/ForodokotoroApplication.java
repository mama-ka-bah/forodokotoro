package com.foro.forordokotoro;

import com.foro.forordokotoro.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.mail.MessagingException;

@SpringBootApplication
public class ForodokotoroApplication {


	@Autowired
	private EmailSenderService senderService;


	public static void main(String[] args) {
		SpringApplication.run(ForodokotoroApplication.class, args);
	}


/*
	@EventListener(ApplicationReadyEvent.class)
	public void triggerMail() throws MessagingException {
		senderService.sendSimpleEmail("touristmali2022@gmail.com",
				"This is email body",
				"This is email subject");

	}

 */



}
