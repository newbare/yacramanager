/**
 * 
 */
package fr.wati.yacramanager.listeners;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import fr.wati.yacramanager.utils.SecurityUtils;

/**
 * @author Rachid Ouattara
 *
 */

@Component
public class WebSockectClientConectedListener implements
		ApplicationListener<SessionConnectedEvent> {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(SessionConnectedEvent event) {
		String userName = (String) ((List)((java.util.Map)event.getMessage().getHeaders().get("nativeHeaders")).get("user-name")).get(0);
		messagingTemplate.convertAndSendToUser(userName, "/user/{username}/queue/errors","Hey !!! Welcome back !!!");
	}

}
