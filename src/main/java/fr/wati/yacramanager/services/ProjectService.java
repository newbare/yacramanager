package fr.wati.yacramanager.services;

import fr.wati.yacramanager.beans.Project;

public interface ProjectService extends CrudService<Project, Long> {
	
	Project createProject(Long clientId, Project project);

	void deleteProject(Long clientId, Long projectId);
}