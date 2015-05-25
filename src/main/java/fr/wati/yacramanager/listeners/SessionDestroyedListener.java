/**
 * 
 */
package fr.wati.yacramanager.listeners;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

/**
 * @author Rachid Ouattara
 *
 */
@Component
public class SessionDestroyedListener implements
		ApplicationListener<HttpSessionDestroyedEvent> {

	@Inject
    private SimpMessageSendingOperations messagingTemplate;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) {
		event.getSecurityContexts();
		messagingTemplate.convertAndSend("/topic/company/event", event);
	}

}
