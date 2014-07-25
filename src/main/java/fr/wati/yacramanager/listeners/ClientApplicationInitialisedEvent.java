/**
 * 
 */
package fr.wati.yacramanager.listeners;

import org.springframework.context.ApplicationEvent;

import fr.wati.yacramanager.beans.Employe;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
public class ClientApplicationInitialisedEvent extends ApplicationEvent {

	private Employe employe;
	/**
	 * @param source
	 */
	public ClientApplicationInitialisedEvent(Employe employe) {
		super(employe);
		this.employe=employe;
	}
	/**
	 * @return the employe
	 */
	public Employe getEmploye() {
		return employe;
	}

	
	
}
