package fr.wati.yacramanager.services.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.dao.ClientRepository;
import fr.wati.yacramanager.dao.ProjectRepository;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.services.TaskService;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private TaskService taskService;

	public Project createProject(Long clientId,Project project){
		Client client=clientRepository.findOne(clientId);
		project.setClient(client);
		project.setCreatedDate(new Date());
		Project saveProject = projectRepository.save(project);
		client.getProjects().add(project);
		/*
		 * Each project should have at least one default Task 
		 */
		Task defaulTask=new Task();
		defaulTask.setCreatedDate(new Date());
		defaulTask.setName("Default Task");
		taskService.createTask(saveProject.getId(), defaulTask);
		return saveProject;
	}
	
	public void deleteProject(Long clientId,Long projectId){
		Client client=clientRepository.findOne(clientId);
		Project project = projectRepository.findByClientAndId(client, projectId);
		projectRepository.delete(project);
	}
	
	@Override
	public <S extends Project> S save(S entity) {
		return projectRepository.save(entity);
	}

	@Override
	public <S extends Project> Iterable<S> save(Iterable<S> entities) {
		return projectRepository.save(entities);
	}

	@Override
	public Project findOne(Long id) {
		return projectRepository.findOne(id);
	}

	@Override
	public boolean exists(Long id) {
		return projectRepository.exists(id);
	}

	@Override
	public Iterable<Project> findAll() {
		return projectRepository.findAll();
	}

	@Override
	public Iterable<Project> findAll(Iterable<Long> ids) {
		return projectRepository.findAll(ids);
	}

	@Override
	public long count() {
		return projectRepository.count();
	}

	@Override
	public void delete(Long id) {
		projectRepository.delete(id);
	}

	@Override
	public void delete(Project entity) {
		projectRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends Project> entities) {
		projectRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		projectRepository.deleteAll();
	}

}
