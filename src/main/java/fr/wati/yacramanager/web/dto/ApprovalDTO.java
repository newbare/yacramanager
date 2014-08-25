/**
 * 
 */
package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rachid Ouattara
 *
 */
public class ApprovalDTO<APPROVAL_ENTITY> {

	private Long employeId;
	private String employeFirstName;
	private String employeLastName;
	private List<APPROVAL_ENTITY> approvalEntities=new ArrayList<>();
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
	 * @return the employeFirstName
	 */
	public String getEmployeFirstName() {
		return employeFirstName;
	}
	/**
	 * @param employeFirstName the employeFirstName to set
	 */
	public void setEmployeFirstName(String employeFirstName) {
		this.employeFirstName = employeFirstName;
	}
	/**
	 * @return the employeLastName
	 */
	public String getEmployeLastName() {
		return employeLastName;
	}
	/**
	 * @param employeLastName the employeLastName to set
	 */
	public void setEmployeLastName(String employeLastName) {
		this.employeLastName = employeLastName;
	}
	/**
	 * @return the approvalEntities
	 */
	public List<APPROVAL_ENTITY> getApprovalEntities() {
		return approvalEntities;
	}
	/**
	 * @param approvalEntities the approvalEntities to set
	 */
	public void setApprovalEntities(List<APPROVAL_ENTITY> approvalEntities) {
		this.approvalEntities = approvalEntities;
	}
	
	
	
}
