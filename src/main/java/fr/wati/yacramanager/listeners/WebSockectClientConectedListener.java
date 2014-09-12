/**
 * 
 */
package fr.wati.yacramanager.listeners;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

/**
 * @author Rachid Ouattara
 *
 */

@Component
public class WebSockectClientConectedListener implements
		ApplicationListener<SessionConnectedEvent> {

	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(SessionConnectedEvent event) {
		String userName = (String) ((List)((java.util.Map)event.getMessage().getHeaders().get("nativeHeaders")).get("user-name")).get(0);
	}

}
