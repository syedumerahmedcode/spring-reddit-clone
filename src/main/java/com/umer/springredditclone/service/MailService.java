package com.umer.springredditclone.service;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.umer.springredditclone.exceptions.SpringRedditException;
import com.umer.springredditclone.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

	private static final String EXCEPTION_OCCURRED_WHEN_SENDING_MAIL_TO = "Exception occurred when sending mail to ";
	private static final String ACTIVATION_MAIL_SENT = "Activation mail sent!!";
	private static final String SPRINGREDDIT_EMAIL_ADDRESS = "springreddit@email.com";
	private final JavaMailSender mailSender;
	private final MailContentBuilder mailContentBuilder;

	@Async
	public void sendMail(NotificationEmail notificationEmail) {

		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom(SPRINGREDDIT_EMAIL_ADDRESS);
			messageHelper.setTo(notificationEmail.getRecipient());
			messageHelper.setSubject(notificationEmail.getSubject());
			messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody())); 
		};
		try {
			mailSender.send(messagePreparator);
			log.info(ACTIVATION_MAIL_SENT);

		} catch (MailException e) {
			throw new SpringRedditException(
					EXCEPTION_OCCURRED_WHEN_SENDING_MAIL_TO + notificationEmail.getRecipient());
		}

	}

}
