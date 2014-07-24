/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
@Entity
public class Employe extends Personne {

	@ManyToOne
	private Company company;
	@OneToMany(mappedBy="employe")
	private List<Absence> absences=new ArrayList<>();
	
	@OneToMany(mappedBy="employe")
	private List<NoteDeFrais> noteDeFrais=new ArrayList<>();

	@ManyToOne
	private Employe manager;
	@OneToMany(mappedBy="manager")
	private List<Employe> managedEmployes=new ArrayList<>();
	
	public List<Absence> getAbsences() {
		return absences;
	}
	public void setAbsences(List<Absence> absences) {
		this.absences = absences;
	}
	/**
	 * @return the company
	 */
	public Company getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
		this.company = company;
	}
	/**
	 * @return the noteDeFrais
	 */
	public List<NoteDeFrais> getNoteDeFrais() {
		return noteDeFrais;
	}
	/**
	 * @param noteDeFrais the noteDeFrais to set
	 */
	public void setNoteDeFrais(List<NoteDeFrais> noteDeFrais) {
		this.noteDeFrais = noteDeFrais;
	}
	public Employe getManager() {
		return manager;
	}
	public void setManager(Employe manager) {
		this.manager = manager;
	}
	public List<Employe> getManagedEmployes() {
		return managedEmployes;
	}
	public void setManagedEmployes(List<Employe> managedEmployes) {
		this.managedEmployes = managedEmployes;
	}
	
	
}
