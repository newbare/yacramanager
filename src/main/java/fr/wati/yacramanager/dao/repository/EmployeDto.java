package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.Gender;

@SuppressWarnings("serial")
public class EmployeDto extends UserDto {

	private String lastName;
	private String firstName;
	private DateTime birthDay;
	private Gender gender;
	private String email;
	private List<String> phoneNumbers;
	private String adress;
	private String postCode;
	private Long companyId;
	private Long managerId;
	private EmployeDto manager;
	private boolean socialUser;
	private String socialUserId;
	private String socialProviderId;
	private String profileImageUrl;
	private String profileUrl;
	
	public EmployeDto() {
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public DateTime getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(DateTime birthDay) {
		this.birthDay = birthDay;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
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
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public EmployeDto getManager() {
		return manager;
	}
	public void setManager(EmployeDto manager) {
		this.manager = manager;
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

}
