/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.io.Serializable;

/**
 * @author Rachid Ouattara
 * 
 */
@SuppressWarnings("serial")
public class EmployesProjectsId implements Serializable{

	private Long employeeId;

	private Long projectId;

	
	
	/**
	 * 
	 */
	public EmployesProjectsId() {
		super();
	}

	/**
	 * @param employeeId
	 * @param projectId
	 */
	public EmployesProjectsId(Long employeeId, Long projectId) {
		super();
		this.employeeId = employeeId;
		this.projectId = projectId;
	}

	/**
	 * @return the employeeId
	 */
	public Long getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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
	
	
}
