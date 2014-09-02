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

}
