/**
 * 
 */
package fr.wati.yacramanager.web.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.beans.Valideable;

/**
 * @author Rachid Ouattara
 *
 */
public class NoteDeFraisDTO implements Valideable {

	private Long id;
	private Long employeId;
	private String employeName;
	private DateTime date;
	private BigDecimal amount;
	private String description;
	private List<Long> attachementsIds=new ArrayList<>();
	private ValidationStatus validationStatus;
	
	
	public NoteDeFrais toNoteDeFrais(NoteDeFrais noteDeFrais){
		noteDeFrais.setAmount(getAmount());
		noteDeFrais.setDate(getDate());
		noteDeFrais.setDescription(getDescription());
		return noteDeFrais;
	}


	/**
	 * @return
	 */
	public NoteDeFrais toNoteDeFrais() {
		NoteDeFrais noteDeFrais=new NoteDeFrais();
		noteDeFrais.setAmount(getAmount());
		noteDeFrais.setDate(getDate());
		noteDeFrais.setDescription(getDescription());
		noteDeFrais.setValidationStatus(getValidationStatus());
		return noteDeFrais;
	}


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
	 * @return the attachementsIds
	 */
	public List<Long> getAttachementsIds() {
		return attachementsIds;
	}


	/**
	 * @param attachementsIds the attachementsIds to set
	 */
	public void setAttachementsIds(List<Long> attachementsIds) {
		this.attachementsIds = attachementsIds;
	}


	/**
	 * @return the employeId
	 */
	public Long getEmployeId() {
		return employeId;
	}


	/**
	 * @param employeId the employeId to set
	 */
	public void setEmployeId(Long employeId) {
		this.employeId = employeId;
	}


	/**
	 * @return the employeName
	 */
	public String getEmployeName() {
		return employeName;
	}


	/**
	 * @param employeName the employeName to set
	 */
	public void setEmployeName(String employeName) {
		this.employeName = employeName;
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
