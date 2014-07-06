/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
@Entity
public class Personne extends Users {


	@NotBlank
	private String nom;
	@NotBlank
	private String prenom;
	@Past
	private Date dateNaissance;
	@Enumerated(EnumType.ORDINAL)
	private Civilite civilite;
	@Embedded
	private Contact contact;
	@OneToMany(mappedBy="personne")
	private List<Absence> absences=new ArrayList<>();
	@ManyToOne
	private Enterprise enterprise;
	@OneToMany(mappedBy="personne")
	private List<NoteDeFrais> noteDeFrais=new ArrayList<>();
	
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}
	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	/**
	 * @return the dateNaissance
	 */
	public Date getDateNaissance() {
		return dateNaissance;
	}
	/**
	 * @param dateNaissance the dateNaissance to set
	 */
	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	/**
	 * @return the civilite
	 */
	public Civilite getCivilite() {
		return civilite;
	}
	/**
	 * @param civilite the civilite to set
	 */
	public void setCivilite(Civilite civilite) {
		this.civilite = civilite;
	}
	/**
	 * @return the contact
	 */
	public Contact getContact() {
		if(contact==null){
			contact=new Contact();
		}
		return contact;
	}
	/**
	 * @param contact the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	public String getFullName(){
		return String.format("%s %s",prenom,nom);
	}
	public List<Absence> getAbsences() {
		return absences;
	}
	public void setAbsences(List<Absence> absences) {
		this.absences = absences;
	}
	
	
}
