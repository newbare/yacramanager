/**
 * 
 */
package fr.wati.yacramanager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.stereotype.Component;

import fr.wati.yacramanager.utils.SecurityUtils;

/**
 * @author Rachid Ouattara
 *
 */
@Component
public class SessionCreatedListener implements
		ApplicationListener<HttpSessionCreatedEvent> {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(HttpSessionCreatedEvent event) {
		if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
			messagingTemplate.convertAndSendToUser(SecurityUtils.getConnectedUser().getUsername(), "/queue/info","Welcome !!!");
			System.out.println(event);
		}
	}

}
