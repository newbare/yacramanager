/**
 * 
 */
package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.beans.TaskStatus;

/**
 * @author Rachid Ouattara
 *
 */
public class TaskDTO extends AuditableDTO{

	private Long id;
	private String name;
	private DateTime createdDate;
	private DateTime lastModifiedDate;
	private String description;
	private Long projectId;
	private TaskStatus taskStatus;
	private Long employeId;
	private String color;
	private ProjectDTO project;
	private List<Long> assignedEmployeesIds=new ArrayList<>();
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
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
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
	 * @return the createdDate
	 */
	public DateTime getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param dateTime the createdDate to set
	 */
	public void setCreatedDate(DateTime dateTime) {
		this.createdDate = dateTime;
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
	 * @return the projectId
	 */
	public Long getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
	 * @return the project
	 */
	public ProjectDTO getProject() {
		return project;
	}
	/**
	 * @param project the project to set
	 */
	public void setProject(ProjectDTO project) {
		this.project = project;
	}
	
	
	public DateTime getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(DateTime modifiedDate) {
		this.lastModifiedDate = modifiedDate;
	}
	public List<Long> getAssignedEmployeesIds() {
		return assignedEmployeesIds;
	}
	public void setAssignedEmployeesIds(List<Long> assignedEmployeesIds) {
		this.assignedEmployeesIds = assignedEmployeesIds;
	}
	public Task toTask(Task task) {
		task.setCreatedDate(getCreatedDate()!=null?getCreatedDate():new DateTime());
		task.setDescription(getDescription());
		if(task.getColor()==null && task.getProject()!=null){
			task.setColor(task.getProject().getColor());
		}
		task.setName(getName());
		task.setTaskStatus(task.getTaskStatus());
		return task;
	}
}
