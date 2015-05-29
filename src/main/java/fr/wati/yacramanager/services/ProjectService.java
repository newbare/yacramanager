package fr.wati.yacramanager.services;

import java.math.BigDecimal;
import java.util.List;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.web.dto.ProjectDTO;

public interface ProjectService extends CrudService<Project, Long>,SpecificationFactory<Project> {
	
	Project createProject(Long clientId, Project project);

	void deleteProject(Long clientId, Long projectId);
	
	Project findByClientAndId(Client client,Long id);
	
	List<Project> findByAssignedEmployeesIn(Employe employe);
	
	/**
	 * @param findByCompanyAndId
	 * @return
	 */
	ProjectDTO toProjectDTO(Project project);
	
	List<ProjectDTO> toProjectDTOs(Iterable<Project> projects);
	
	long countNumberOfEmployeeForProject(Project project);
	
	long countNumberOfTaskForProject(Project project);
	
	void assignEmployeToProject(Long projectId,Long employeId,boolean teamLead,BigDecimal dailyRate) throws ServiceException;
	
	void unassignEmployeFromProject(Long projectId,Long employeId) throws ServiceException;
}
