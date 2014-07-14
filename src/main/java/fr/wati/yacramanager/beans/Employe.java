/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
@Entity
public class Employe extends Personne {

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "projects_employees", joinColumns = { @JoinColumn(name = "employeId") }, inverseJoinColumns = { @JoinColumn(name = "projectId") })
	private List<Project> projects;
	@OneToMany(mappedBy="employe")
	private List<Task> tasks;
	@OneToMany(mappedBy="employe")
	private List<WorkLog> workLogs;
	@ManyToOne
	private Company company;

	/**
	 * @return the projects
	 */
	public List<Project> getProjects() {
		return projects;
	}

	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<Project> projects) {
		this.projects = projects;
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

	/**
	 * @return the workLogs
	 */
	public List<WorkLog> getWorkLogs() {
		return workLogs;
	}

	/**
	 * @param workLogs the workLogs to set
	 */
	public void setWorkLogs(List<WorkLog> workLogs) {
		this.workLogs = workLogs;
	}
	
	
}
