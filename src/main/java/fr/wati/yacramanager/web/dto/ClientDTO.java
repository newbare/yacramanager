package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.Client;

public class ClientDTO {
	private Long id;
	private String name;
	private Long companyId;
	private DateTime createdDate;
	private List<ContactDTO> contacts = new ArrayList<>();
	private List<ProjectDTO> projects=new ArrayList<>();
	
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
	
	public Client toClient(Client client){
		client.setName(getName());
		return client;
	}
	
	public Client toClient(){
		return toClient(new Client());
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public List<ContactDTO> getContacts() {
		return contacts;
	}
	public void setContacts(List<ContactDTO> contacts) {
		this.contacts = contacts;
	}
	public List<ProjectDTO> getProjects() {
		return projects;
	}
	public void setProjects(List<ProjectDTO> projects) {
		this.projects = projects;
	}
	public DateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	
}
