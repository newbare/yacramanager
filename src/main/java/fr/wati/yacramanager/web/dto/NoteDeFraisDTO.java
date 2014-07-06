/**
 * 
 */
package fr.wati.yacramanager.web.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.wati.yacramanager.beans.NoteDeFrais;

/**
 * @author Rachid Ouattara
 *
 */
public class NoteDeFraisDTO {

	private Long id;
	private Date date;
	private BigDecimal amount;
	private String description;
	private List<AttachementDTO> attachements=new ArrayList<>();
	
	
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
	public List<AttachementDTO> getAttachements() {
		return attachements;
	}


	/**
	 * @param attachements the attachements to set
	 */
	public void setAttachements(List<AttachementDTO> attachements) {
		this.attachements = attachements;
	}
	
	
}
