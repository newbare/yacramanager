package fr.wati.yacramanager.web.dto;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

import fr.wati.yacramanager.beans.EmployesProjects;
import fr.wati.yacramanager.beans.EmployesProjectsId;
import fr.wati.yacramanager.dao.repository.EmployeDto;

public class EmployesProjectsDTO {

	private EmployesProjectsId id;
	private EmployeDto employe;
	private ProjectDTO project;
	private LocalDate joinDate;
	private LocalDate leaveDate;
	private BigDecimal dailyRate;
	
	public EmployesProjectsDTO() {
	}

	public EmployesProjects toEmployeesProjects(EmployesProjects employesProjects) {
		return employesProjects;
	}

	public EmployesProjectsId getId() {
		return id;
	}

	public void setId(EmployesProjectsId id) {
		this.id = id;
	}

	public EmployeDto getEmploye() {
		return employe;
	}

	public void setEmploye(EmployeDto employe) {
		this.employe = employe;
	}

	public ProjectDTO getProject() {
		return project;
	}

	public void setProject(ProjectDTO project) {
		this.project = project;
	}

	public LocalDate getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}

	public LocalDate getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(LocalDate leaveDate) {
		this.leaveDate = leaveDate;
	}

	public BigDecimal getDailyRate() {
		return dailyRate;
	}

	public void setDailyRate(BigDecimal dailyRate) {
		this.dailyRate = dailyRate;
	}

	
	
}
