package fr.wati.yacramanager.web.dto;

import fr.wati.yacramanager.beans.Client;

public class ClientDTO {
	private Long id;
	private String name;
	private Long companyId;
	
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
	
}
