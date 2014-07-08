/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
@Entity
public class NoteDeFrais implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date date;
	private BigDecimal amount;
	private String description;
	@OneToMany(mappedBy="noteDeFrais")
	private List<Attachement> attachements=new ArrayList<>();
	@ManyToOne
	private Personne personne;
	
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * @return the attachements
	 */
	public List<Attachement> getAttachements() {
		return attachements;
	}
	/**
	 * @param attachements the attachements to set
	 */
	public void setAttachements(List<Attachement> attachements) {
		this.attachements = attachements;
	}
	/**
	 * @return the personne
	 */
	public Personne getPersonne() {
		return personne;
	}
	/**
	 * @param personne the personne to set
	 */
	public void setPersonne(Personne personne) {
		this.personne = personne;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param attachement
	 */
	public void addAttachement(Attachement attachement) {
		this.attachements.add(attachement);
	}
	
	public void removeAttachement(Attachement attachement) {
		this.attachements.remove(attachement);
	}
	
	
	
}
