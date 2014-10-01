/**
 * 
 */
package fr.wati.yacramanager.beans;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Past;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Personne extends Users {


	private String lastName;
	private String firstName;
	@Past
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime birthDay;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="contactId")
	private Contact contact;
	
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the birthDay
	 */
	public DateTime getBirthDay() {
		return birthDay;
	}
	/**
	 * @param dateNaissance the birthDay to set
	 */
	public void setBirthDay(DateTime birthDay) {
		this.birthDay = birthDay;
	}
	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}
	/**
	 * @param contact the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	public String getFullName(){
		return String.format("%s %s",firstName,lastName);
	}
	
}
