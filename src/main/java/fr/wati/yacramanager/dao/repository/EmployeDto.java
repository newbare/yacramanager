package fr.wati.yacramanager.dao.repository;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.Gender;

@SuppressWarnings("serial")
public class EmployeDto extends UserDto {

	private String lastName;
	private String firstName;
	private DateTime birthDay;
	private Gender gender;
	private String email;
	private String numeroTelephone;
	private String rue;
	private String codePostal;
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
	public String getNumeroTelephone() {
		return numeroTelephone;
	}
	public void setNumeroTelephone(String numeroTelephone) {
		this.numeroTelephone = numeroTelephone;
	}
	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue;
	}
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
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
