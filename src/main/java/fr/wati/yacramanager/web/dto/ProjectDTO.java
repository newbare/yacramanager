/**
 * 
 */
package fr.wati.yacramanager.web.dto;

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
	private ClientDTO client;
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
	 * @param project
	 * @return 
	 */
	public Project toProject(Project project) {
		project.setCreatedDate(getCreatedDate()!=null?getCreatedDate():new DateTime());
		project.setDescription(getDescription());
		project.setName(getName());
		return project;
	}
	
	
}
