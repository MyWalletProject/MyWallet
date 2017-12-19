package com.mywallet.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mywallet.controller.UserController;
import com.mywallet.domain.User;

@Service
public class MailService {


	@Autowired
	private JavaMailSender javaMailSender;

	private static final Logger logger = LoggerFactory.getLogger(MailService.class);

	int threads = 20;
	ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(threads);

	public void sendMail(String to, String subject, String body) 
	{
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setTo(to);
		simpleMailMessage.setText(body);
		scheduledExecutorService.submit(new Runnable() {

			@Override
			public void run() {
				try{
					javaMailSender.send(simpleMailMessage);
					logger.info("sending mail to user successfully :");
				}catch(Exception e){
					logger.error(e.getMessage(),e);
				}
			}
		});

		/*MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper=null;
        try {
			helper= new MimeMessageHelper(message, true);

			helper.setSubject(subject);
	        helper.setTo(to);
            helper.setText(body,true);

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}


	public void userRegisterMail(User user){
		sendMail(user.getEmail(),"MyWallet Register ","Successfully registered on mywallet");
	}

}
