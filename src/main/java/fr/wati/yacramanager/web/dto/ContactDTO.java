package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.List;

import fr.wati.yacramanager.beans.Adresse;

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

}
