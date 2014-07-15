package fr.wati.yacramanager.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

@SuppressWarnings("serial")
@Entity
public class Company implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	@OneToMany(mappedBy="company")
	private List<Employe> employes=new ArrayList<>();
	@OneToMany(mappedBy="company")
	private List<Client> clients=new ArrayList<>();
	private Date registeredDate;
	private Date licenseEndDate;
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(
	        name="CONTACT",
	        joinColumns=@JoinColumn(name="OWNER_ID")
	  )
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
	public List<Employe> getEmployes() {
		return employes;
	}
	public void setEmployes(List<Employe> employes) {
		this.employes = employes;
	}
	public List<Client> getClients() {
		return clients;
	}
	public void setClients(List<Client> clients) {
		this.clients = clients;
	}
	@PreRemove
	public void preRemove(){
		setClients(null);
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
	
}
