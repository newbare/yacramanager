package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Contact;

public class CompanyDTO {

	private Long id;
	private String name;
	private DateTime licenseEndDate;
	private DateTime registeredDate;
	private List<ContactDTO> contacts=new ArrayList<>();
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
		company.setRegisteredDate(getRegisteredDate());
		List<Contact> contacts=new ArrayList<>();
		for(ContactDTO contactDTO: getContacts()){
			Contact contact=new Contact();
			contact.setName(contactDTO.getName());
			contact.setEmail(contactDTO.getEmail());
			contact.setPhoneNumbers(contactDTO.getPhoneNumbers());
			contacts.add(contact);
		}
		company.setContacts(contacts);
		return company;
	}
	/**
	 * @return the licenseEndDate
	 */
	public DateTime getLicenseEndDate() {
		return licenseEndDate;
	}
	/**
	 * @param licenseEndDate the licenseEndDate to set
	 */
	public void setLicenseEndDate(DateTime licenseEndDate) {
		this.licenseEndDate = licenseEndDate;
	}
	/**
	 * @return the contacts
	 */
	public List<ContactDTO> getContacts() {
		return contacts;
	}
	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(List<ContactDTO> contacts) {
		this.contacts = contacts;
	}
	/**
	 * @return the registeredDate
	 */
	public DateTime getRegisteredDate() {
		return registeredDate;
	}
	/**
	 * @param registeredDate the registeredDate to set
	 */
	public void setRegisteredDate(DateTime registeredDate) {
		this.registeredDate = registeredDate;
	}
	
	
}
