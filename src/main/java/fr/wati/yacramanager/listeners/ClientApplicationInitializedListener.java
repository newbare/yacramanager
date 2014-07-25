/**
 * 
 */
package fr.wati.yacramanager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Rachid Ouattara
 *
 */
@Component
public class ClientApplicationInitializedListener implements
		ApplicationListener<ClientApplicationInitialisedEvent> {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ClientApplicationInitialisedEvent event) {
		messagingTemplate.convertAndSendToUser(event.getEmploye().getUsername(), "/queue/errors","Hey !!! Welcome back !!!");
	}

}
