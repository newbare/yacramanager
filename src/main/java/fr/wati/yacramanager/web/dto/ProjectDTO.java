/**
 * 
 */
package fr.wati.yacramanager.web.dto;

import java.util.Date;

import fr.wati.yacramanager.beans.Project;

/**
 * @author Rachid Ouattara
 *
 */
public class ProjectDTO {

	private Long id;
	private String name;
	private String description;
	private Date createdDate;
	private ClientDTO clientDTO;
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
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the clientDTO
	 */
	public ClientDTO getClientDTO() {
		return clientDTO;
	}
	/**
	 * @param clientDTO the clientDTO to set
	 */
	public void setClientDTO(ClientDTO clientDTO) {
		this.clientDTO = clientDTO;
	}
	/**
	 * @param project
	 * @return 
	 */
	public Project toProject(Project project) {
		project.setCreatedDate(project.getCreatedDate());
		project.setDescription(project.getDescription());
		project.setName(project.getName());
		return project;
	}
	
	
}
