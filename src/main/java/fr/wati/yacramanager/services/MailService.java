/**
 * 
 */
package fr.wati.yacramanager.services;

import java.util.Locale;

/**
 * @author Rachid Ouattara
 *
 */
public interface MailService {

	void sendEmail(String to, String subject, String content,
			boolean isMultipart, boolean isHtml);

	void sendActivationEmail(String email, String content,
			Locale locale);

}