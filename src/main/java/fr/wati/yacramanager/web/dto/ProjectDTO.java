/**
 * 
 */
package fr.wati.yacramanager.web.dto;

import java.util.List;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.Project;

/**
 * @author Rachid Ouattara
 *
 */
public class ProjectDTO {

	private Long id;
	private String name;
	private String description;
	private DateTime createdDate;
	private String color;
	private ClientDTO client;
	private List<TaskDTO> tasks;
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
	 * @return the createdDate
	 */
	public DateTime getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the clientDTO
	 */
	public ClientDTO getClient() {
		return client;
	}
	/**
	 * @param clientDTO the clientDTO to set
	 */
	public void setClient(ClientDTO clientDTO) {
		this.client = clientDTO;
	}
	
	
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	
	
	
	public List<TaskDTO> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskDTO> tasks) {
		this.tasks = tasks;
	}
	/**
	 * @param project
	 * @return 
	 */
	public Project toProject(Project project) {
		project.setCreatedDate(getCreatedDate()!=null?getCreatedDate():new DateTime());
		project.setDescription(getDescription());
		project.setColor(getColor());
		project.setName(getName());
		return project;
	}
	
	
}
