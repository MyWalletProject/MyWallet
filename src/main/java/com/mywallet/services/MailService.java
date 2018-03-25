package com.mywallet.services;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.mywallet.domain.User;

@Service
public class MailService {


	@Autowired
	private JavaMailSender javaMailSender;

	private static final Logger logger = LoggerFactory.getLogger(MailService.class);
	
	
	public MailService(){
			logger.info("MailService class Bean is created : ");
	}

	int threads = 20;
	 ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(threads);

	public void sendMail(String from,String password,String to,String sub,String msg){  
		//Get properties object    
		Properties props = new Properties();    
		props.put("mail.smtp.host", "smtp.gmail.com");    
		props.put("mail.smtp.socketFactory.port","465");    
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");    
		props.put("mail.smtp.port", "465");    
		//get Session   
		Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {    
			protected PasswordAuthentication getPasswordAuthentication() {    
				return new PasswordAuthentication(from,password);  
			}    
		});    
		//compose message    
		try {    
			MimeMessage message = new MimeMessage(session);    
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    	
			message.setSubject(sub);    
			message.setText(msg); 
			
			scheduledExecutorService.submit(new Runnable() {
				
				@Override
				public void run() {
					try{
						javaMailSender.send(message);
						logger.info("Sending mail to user successfully :");
					}catch(Exception e){
						logger.error(e.getMessage(),e);
					}
				}
			});				
			//send message  
			Transport.send(message);    
			System.out.println("message sent successfully");    
		} catch (MessagingException e) {throw new RuntimeException(e);}    

	}  

	public void userRegisterMail(User user){
		sendMail(null, null,user.getEmail(),"MyWallet Register ","Successfully registered on mywallet");
	}
	
//	public void sendMail(String to, String subject, String body ) 
//	{
//		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//		simpleMailMessage.setSubject(subject);
//		simpleMailMessage.setTo(to);
//		simpleMailMessage.setText(body);
//		scheduledExecutorService.submit(new Runnable() {
//
//			@Override
//			public void run() {
//				try{
//					javaMailSender.send(simpleMailMessage);
//					logger.info("Sending mail to user successfully :");
//				}catch(Exception e){
//					logger.error(e.getMessage(),e);
//				}
//			}
//		});
//
//	}


}
