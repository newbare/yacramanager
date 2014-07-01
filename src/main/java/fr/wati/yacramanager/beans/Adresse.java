/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
@Embeddable
public class Adresse implements Serializable {

	
	private String rue;
	private String codePostal;
	
	/**
	 * @return the rue
	 */
	public String getRue() {
		return rue;
	}
	/**
	 * @param rue the rue to set
	 */
	public void setRue(String rue) {
		this.rue = rue;
	}
	/**
	 * @return the codePostal
	 */
	public String getCodePostal() {
		return codePostal;
	}
	/**
	 * @param codePostal the codePostal to set
	 */
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	
	
}
