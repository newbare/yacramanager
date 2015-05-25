/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employe extends Personne {

	@ManyToOne
	private Company company;
	@OneToMany(mappedBy="employe",cascade={CascadeType.REMOVE})
	private List<Absence> absences=new ArrayList<>();
	
	@OneToMany(mappedBy="employe" ,cascade={CascadeType.REMOVE})
	private List<NoteDeFrais> noteDeFrais=new ArrayList<>();
	
	@OneToMany(mappedBy="employe" ,cascade={CascadeType.REMOVE})
	private List<WorkLog> workLogs=new ArrayList<>();

	@ManyToOne
	private Employe manager;
	@OneToMany(mappedBy="manager")
	private List<Employe> managedEmployes=new ArrayList<>();
	
	@OneToMany
	private List<EmployesProjects> projects=new ArrayList<>();
	
	@ManyToMany(mappedBy="assignedEmployees")
	private List<Task> tasks=new ArrayList<>();
	
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
	
	/**
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}
	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public List<WorkLog> getWorkLogs() {
		return workLogs;
	}
	public void setWorkLogs(List<WorkLog> workLogs) {
		this.workLogs = workLogs;
	}
	/**
	 * @return the projects
	 */
	public List<EmployesProjects> getProjects() {
		return projects;
	}
	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<EmployesProjects> projects) {
		this.projects = projects;
	}
	
	
}
