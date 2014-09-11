/**
 * 
 */
package fr.wati.yacramanager.listeners;

import org.springframework.context.ApplicationEvent;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
public class ExceptionEvent extends ApplicationEvent{
	private Throwable throwable;

	/**
	 * @param source
	 * @param throwable
	 */
	public ExceptionEvent(Object source, Throwable throwable) {
		super(source);
		this.throwable = throwable;
	}

	/**
	 * @return the throwable
	 */
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * @param throwable the throwable to set
	 */
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	};
	
	
	
}
