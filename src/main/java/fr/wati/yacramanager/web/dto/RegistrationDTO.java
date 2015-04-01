/**
 * 
 */
package fr.wati.yacramanager.web.dto;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.Gender;

/**
 * @author Rachid Ouattara
 *
 */
public class RegistrationDTO {

	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String companyName;
	private DateTime birthDay;
	private Gender gender;
	private boolean socialUser;
	private String socialUserId;
	private String socialProviderId;
	private String profileImageUrl;
	private String profileUrl;
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public boolean isSocialUser() {
		return socialUser;
	}
	public void setSocialUser(boolean socialUser) {
		this.socialUser = socialUser;
	}
	public String getSocialUserId() {
		return socialUserId;
	}
	public void setSocialUserId(String socialUserId) {
		this.socialUserId = socialUserId;
	}
	public String getSocialProviderId() {
		return socialProviderId;
	}
	public void setSocialProviderId(String socialProviderId) {
		this.socialProviderId = socialProviderId;
	}
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	public String getProfileUrl() {
		return profileUrl;
	}
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	public DateTime getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(DateTime birthDay) {
		this.birthDay = birthDay;
	}
	
	
	
}
