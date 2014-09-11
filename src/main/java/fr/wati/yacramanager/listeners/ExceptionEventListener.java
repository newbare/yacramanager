/**
 * 
 */
package fr.wati.yacramanager.listeners;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import fr.wati.yacramanager.services.MailService;

/**
 * @author Rachid Ouattara
 *
 */
@Component
public class ExceptionEventListener implements
		ApplicationListener<ExceptionEvent> {

	private final Logger logger=LoggerFactory.getLogger(ExceptionEventListener.class);
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private Environment environment;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ExceptionEvent event) {
		logger.error(event.getThrowable().getMessage(), event.getThrowable());
		StringWriter mailContent=new StringWriter();
		ExceptionUtils.printRootCauseStackTrace(event.getThrowable(), new PrintWriter(mailContent));
		mailService.sendEmail(environment.getProperty("application.exception.report.mail.to"), environment.getProperty("application.exception.report.mail.subject"), mailContent.getBuffer().toString(), false, true);
	}

}
