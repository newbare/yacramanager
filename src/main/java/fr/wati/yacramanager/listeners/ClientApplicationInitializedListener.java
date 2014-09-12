/**
 * 
 */
package fr.wati.yacramanager.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Rachid Ouattara
 *
 */
@Component
public class ClientApplicationInitializedListener implements
		ApplicationListener<ClientApplicationInitialisedEvent> {

	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ClientApplicationInitialisedEvent event) {
	}

}
