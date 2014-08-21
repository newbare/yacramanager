/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
@Entity
public class NoteDeFrais extends AuditableEntity implements Valideable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime date;
	private BigDecimal amount;
	private String description;
	@OneToMany(mappedBy="noteDeFrais")
	private List<Attachement> attachements=new ArrayList<>();
	@ManyToOne
	private Employe employe;
	@Enumerated(EnumType.STRING)
	private ValidationStatus validationStatus;
	
	
	
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
	public DateTime getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(DateTime date) {
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
	public Employe getEmploye() {
		return employe;
	}
	/**
	 * @param employe the personne to set
	 */
	public void setEmploye(Employe employe) {
		this.employe = employe;
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
	
	@Override
	public ValidationStatus getValidationStatus() {
		return validationStatus;
	}

	@Override
	public void setValidationStatus(ValidationStatus validationStatus) {
		this.validationStatus=validationStatus;
	}
	
}
