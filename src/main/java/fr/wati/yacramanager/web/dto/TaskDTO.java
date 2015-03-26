/**
 * 
 */
package fr.wati.yacramanager.web.dto;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.beans.TaskStatus;

/**
 * @author Rachid Ouattara
 *
 */
public class TaskDTO {

	private Long id;
	private String name;
	private DateTime createdDate;
	private String description;
	private Long projectId;
	private TaskStatus taskStatus;
	private Long employeId;
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
