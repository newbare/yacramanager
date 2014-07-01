/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.hibernate.validator.constraints.Email;

/**
 * @author Rachid Ouattara
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class Contact implements Serializable {

	@Email
	private String email;

	private String numeroTelephone;
	@Embedded
	private Adresse adresse;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumeroTelephone() {
		return numeroTelephone;
	}

	public void setNumeroTelephone(String numeroTelephone) {
		this.numeroTelephone = numeroTelephone;
	}

	public Adresse getAdresse() {
		if (adresse == null) {
			adresse = new Adresse();
		}
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

}
