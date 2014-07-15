package fr.wati.yacramanager.web.dto;

import fr.wati.yacramanager.beans.Company;

public class CompanyDTO {

	private Long id;
	private String name;
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
		return company;
	}
	
	
}
