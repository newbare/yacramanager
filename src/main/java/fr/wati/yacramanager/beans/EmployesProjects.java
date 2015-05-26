/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

/**
 * @author Rachid Ouattara
 * 
 */
@Entity
@IdClass(EmployesProjectsId.class)
public class EmployesProjects {

	@Id
	private Long employeeId;
	@Id
	private Long projectId;
	private boolean projectLead;

	private BigDecimal dailyRate;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate joinDate;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate leaveDate;
	@ManyToOne
	@PrimaryKeyJoinColumn(name = "EMPLOYEEID", referencedColumnName = "ID")
	private Employe employee;
	@ManyToOne
	@PrimaryKeyJoinColumn(name = "PROJECTID", referencedColumnName = "ID")
	private Project project;
	
	
	
	/**
	 * 
	 */
	public EmployesProjects() {
		super();
	}


	/**
	 * @param employee
	 * @param project
	 * @param projectLead
	 * @param dailyRate
	 * @param joinDate
	 */
	public EmployesProjects(Employe employee, Project project,
			boolean projectLead, BigDecimal dailyRate, LocalDate joinDate) {
		super();
		this.employee = employee;
		this.employeeId=employee.getId();
		this.project = project;
		this.projectId=project.getId();
		this.projectLead = projectLead;
		this.dailyRate = dailyRate;
		this.joinDate = joinDate;
	}
	
	
	/**
	 * @param employee
	 * @param project
	 * @param projectLead
	 * @param dailyRate
	 */
	public EmployesProjects(Employe employee, Project project,
			boolean projectLead, BigDecimal dailyRate) {
		this(employee, project, projectLead, dailyRate, new LocalDate());
	}
	
	/**
	 * @param employee
	 * @param project
	 * @param projectLead
	 */
	public EmployesProjects(Employe employee, Project project,
			boolean projectLead) {
		this(employee, project, projectLead, BigDecimal.ZERO, new LocalDate());
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
	/**
	 * @return the projectLead
	 */
	public boolean isProjectLead() {
		return projectLead;
	}
	/**
	 * @param projectLead the projectLead to set
	 */
	public void setProjectLead(boolean projectLead) {
		this.projectLead = projectLead;
	}
	/**
	 * @return the dailyRate
	 */
	public BigDecimal getDailyRate() {
		return dailyRate;
	}
	/**
	 * @param dailyRate the dailyRate to set
	 */
	public void setDailyRate(BigDecimal dailyRate) {
		this.dailyRate = dailyRate;
	}
	/**
	 * @return the joinDate
	 */
	public LocalDate getJoinDate() {
		return joinDate;
	}
	/**
	 * @param joinDate the joinDate to set
	 */
	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}
	/**
	 * @return the employee
	 */
	public Employe getEmployee() {
		return employee;
	}
	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employe employee) {
		this.employee = employee;
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


	public LocalDate getLeaveDate() {
		return leaveDate;
	}


	public void setLeaveDate(LocalDate leaveDate) {
		this.leaveDate = leaveDate;
	}

}
