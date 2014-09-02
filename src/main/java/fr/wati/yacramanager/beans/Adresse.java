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

	
	private String adress;
	private String postCode;
	private String city;
	private String country;
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
	
	
	
}
