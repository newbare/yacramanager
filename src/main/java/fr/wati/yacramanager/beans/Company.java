package fr.wati.yacramanager.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Company extends AuditableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	@OneToMany(mappedBy="company")
	private List<Employe> employes=new ArrayList<>();
	@OneToMany(mappedBy="company")
	private List<Client> clients=new ArrayList<>();
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime registeredDate;
	@OneToOne
	private CompanyAccountInfo companyAccountInfo;
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="companyId")
	private List<Contact> contacts=new ArrayList<>();
	@OneToMany
	@JoinColumn(name="companyId", referencedColumnName="id")
	private List<Settings> settings=new ArrayList<>();
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private byte[] logo;
	
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
	
	public CompanyAccountInfo getCompanyAccountInfo() {
		return companyAccountInfo;
	}
	
	public void setCompanyAccountInfo(CompanyAccountInfo companyAccountInfo) {
		this.companyAccountInfo = companyAccountInfo;
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
	 * @return the settings
	 */
	public List<Settings> getSettings() {
		return settings;
	}
	/**
	 * @param settings the settings to set
	 */
	public void setSettings(List<Settings> settings) {
		this.settings = settings;
	}
	/**
	 * @return the logo
	 */
	public byte[] getLogo() {
		return logo;
	}
	/**
	 * @param logo the logo to set
	 */
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	
}
