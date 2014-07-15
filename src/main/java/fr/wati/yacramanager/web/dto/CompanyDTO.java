package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Contact;

public class CompanyDTO {

	private Long id;
	private String name;
	private Date licenseEndDate;
	private Date registeredDate;
	private List<Contact> contacts=new ArrayList<>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Company toCompany(Company company) {
		company.setName(getName());
		company.setLicenseEndDate(getLicenseEndDate());
		return company;
	}
	/**
	 * @return the licenseEndDate
	 */
	public Date getLicenseEndDate() {
		return licenseEndDate;
	}
	/**
	 * @param licenseEndDate the licenseEndDate to set
	 */
	public void setLicenseEndDate(Date licenseEndDate) {
		this.licenseEndDate = licenseEndDate;
	}
	/**
	 * @return the contacts
	 */
	public List<Contact> getContacts() {
		return contacts;
	}
	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	/**
	 * @return the registeredDate
	 */
	public Date getRegisteredDate() {
		return registeredDate;
	}
	/**
	 * @param registeredDate the registeredDate to set
	 */
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
	
	
}
