package fr.wati.yacramanager.dao.repository;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.Civilite;

@SuppressWarnings("serial")
public class EmployeDto extends UserDto {

	private String nom;
	private String prenom;
	private DateTime dateNaissance;
	private Civilite civilite;
	private String email;
	private String numeroTelephone;
	private String rue;
	private String codePostal;
	private Long companyId;
	private Long managerId;
	
	public EmployeDto() {
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public DateTime getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(DateTime dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	public Civilite getCivilite() {
		return civilite;
	}
	public void setCivilite(Civilite civilite) {
		this.civilite = civilite;
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
