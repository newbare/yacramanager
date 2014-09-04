package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.List;

import fr.wati.yacramanager.beans.Adresse;
import fr.wati.yacramanager.beans.Contact;

public class ContactDTO {

	private Long id;
	private String name;
	private String email;
	private List<String> phoneNumbers=new ArrayList<>();
	private Adresse adresse;
	
	
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
	public Adresse getAdresse() {
		return adresse;
	}
	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public Contact toContact(Contact contact){
		if(null==contact.getId()){
			contact.setId(getId());
		}
		contact.setName(getName());
		contact.setEmail(getEmail());
		contact.setAdresse(getAdresse());
		contact.setPhoneNumbers(getPhoneNumbers());
		return contact;
	}
	public Contact toContact(){
		return toContact(new Contact());
	}
}
