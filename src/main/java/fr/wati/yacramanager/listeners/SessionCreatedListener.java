/**
 * 
 */
package fr.wati.yacramanager.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.stereotype.Component;

/**
 * @author Rachid Ouattara
 *
 */
@Component
public class SessionCreatedListener implements
		ApplicationListener<HttpSessionCreatedEvent> {

	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(HttpSessionCreatedEvent event) {
		if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
			System.out.println(event);
		}
	}

}
