/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
public class Task extends AuditableEntity  {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;
	@ManyToOne
	private Project project;
	@Enumerated(EnumType.STRING)
	private TaskStatus taskStatus=TaskStatus.OPEN;
	@OneToMany(mappedBy="task")
	private List<WorkLog> workLogs;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "tasks_employees",
			joinColumns = { @JoinColumn(name = "taskId", referencedColumnName = "id") },
			inverseJoinColumns = { @JoinColumn(name = "employeId", referencedColumnName = "id") })
	private List<Employe> assignedEmployees = new ArrayList<>();
	
	private String color;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}
	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}
	/**
	 * @return the taskStatus
	 */
	public TaskStatus getTaskStatus() {
		return taskStatus;
	}
	/**
	 * @param taskStatus the taskStatus to set
	 */
	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
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
	
	/**
	 * @return the assignedEmployees
	 */
	public List<Employe> getAssignedEmployees() {
		return assignedEmployees;
	}
	/**
	 * @param assignedEmployees the assignedEmployees to set
	 */
	public void setAssignedEmployees(List<Employe> assignedEmployees) {
		this.assignedEmployees = assignedEmployees;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
	
}
