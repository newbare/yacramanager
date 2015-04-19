package fr.wati.yacramanager.config.metrics;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import com.codahale.metrics.health.HealthCheck;

@Component
public class MailHealthCheck extends HealthCheck implements InitializingBean {

	private final Logger log = LoggerFactory.getLogger(MailHealthCheck.class);
	@Inject
	private JavaMailSenderImpl javaMailSender;


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codahale.metrics.health.HealthCheck#check()
	 */
	@Override
	protected Result check() throws Exception {
		log.debug("Initializing JavaMail health indicator");
		try {
			javaMailSender
					.getSession()
					.getTransport()
					.connect(javaMailSender.getHost(),
							javaMailSender.getPort(),
							javaMailSender.getUsername(),
							javaMailSender.getPassword());

			return Result.healthy("Mail service is up !");

		} catch (MessagingException e) {
			log.debug("Cannot connect to e-mail server.", e);
			return Result.unhealthy(e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

}
