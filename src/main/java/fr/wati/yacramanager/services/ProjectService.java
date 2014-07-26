package fr.wati.yacramanager.services;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.web.dto.ProjectDTO;

public interface ProjectService extends CrudService<Project, Long>,SpecificationFactory<Project> {
	
	Project createProject(Long clientId, Project project);

	void deleteProject(Long clientId, Long projectId);
	
	Project findByClientAndId(Client client,Long id);
	
	/**
	 * @param findByCompanyAndId
	 * @return
	 */
	ProjectDTO toProjectDTO(Project findByCompanyAndId);
}
