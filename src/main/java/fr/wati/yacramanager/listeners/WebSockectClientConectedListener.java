/**
 * 
 */
package fr.wati.yacramanager.listeners;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import fr.wati.yacramanager.services.security.CustomUserDetailsService.CustomUserDetails;

/**
 * @author Rachid Ouattara
 *
 */

@Component
public class WebSockectClientConectedListener implements
		ApplicationListener<SessionConnectedEvent> {
	 private final Logger log = LoggerFactory.getLogger(WebSockectClientConectedListener.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(SessionConnectedEvent event) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
		Principal user = StompHeaderAccessor.getUser(headers.getMessageHeaders());
		if(user instanceof UsernamePasswordAuthenticationToken){
			CustomUserDetails customUserDetails= (CustomUserDetails) (((UsernamePasswordAuthenticationToken)user).getPrincipal());
			log.debug(customUserDetails.getDomainUser().getUsername()+" just log in...");
		}
	}

}
