package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Activities.ActivityOperation;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.dao.repository.ProjectRepository;
import fr.wati.yacramanager.dao.repository.TaskRepository;
import fr.wati.yacramanager.listeners.ActivityEvent;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.TaskService;
import fr.wati.yacramanager.utils.Filter;

@Transactional
@Service
public class TaskServiceImpl implements TaskService {

	@Inject
	private TaskRepository taskRepository;
	@Inject
	private ProjectRepository  projectRepository;
	@Inject
	private EmployeService employeService;
	
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public <S extends Task> S save(S entity) {
		ActivityOperation activityOperation=entity.getId()==null?ActivityOperation.CREATE:ActivityOperation.UPDATE;
		S save = taskRepository.save(entity);
		applicationEventPublisher.publishEvent(ActivityEvent
				.createWithSource(this).user()
				.operation(activityOperation)
				.onEntity(Task.class, save.getId()));
		return save;
	}

	@Override
	public <S extends Task> Iterable<S> save(Iterable<S> entities) {
		return taskRepository.save(entities);
	}

	@Override
	public Task findOne(Long id) {
		return taskRepository.findOne(id);
	}

	@Override
	public boolean exists(Long id) {
		return taskRepository.exists(id);
	}

	@Override
	public Iterable<Task> findAll() {
		return taskRepository.findAll();
	}

	@Override
	public Iterable<Task> findAll(Iterable<Long> ids) {
		return taskRepository.findAll(ids);
	}

	@Override
	public long count() {
		return taskRepository.count();
	}

	@Override
	public void delete(Long id) {
		taskRepository.delete(id);
		applicationEventPublisher.publishEvent(ActivityEvent
				.createWithSource(this).user()
				.operation(ActivityOperation.DELETE)
				.onEntity(Task.class, id));
	}

	@Override
	public void delete(Task entity) {
		taskRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends Task> entities) {
		taskRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		taskRepository.deleteAll();
	}

	@Override
	public Task createTask(Long projectId, Task task) {
		Project project=projectRepository.findOne(projectId);
		task.setProject(project);
		if(StringUtils.isEmpty(task.getColor())){
			task.setColor(project.getColor());
		}
		Task saveTask = save(task);
		project.getTasks().add(saveTask);
		return saveTask;
	}

	@Override
	public void deleteTask(Long projectId, Long taskId) {
		Project project=projectRepository.findOne(projectId);
		Task task = taskRepository.findByProjectAndId(project, taskId);
		taskRepository.delete(task);
	}

	@Override
	public Page<Task> findAll(Specification<Task> spec, Pageable pageable) {
		return taskRepository.findAll(spec, pageable);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.TaskService#findByProjectAndEmploye(fr.wati.yacramanager.beans.Project, fr.wati.yacramanager.beans.Employe)
	 */
	@Override
	public List<Task> findByProjectAndAssignedEmployeesIn(Project project, Employe employe) {
		return taskRepository.findByProjectAndAssignedEmployeesIn(project, employe);
	}

	@Override
	public Specification<Task> buildSpecification(Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	@Override
	@Transactional
	public void assignEmployeToTask(Long employeId, Long taskId) {
		Employe employe = employeService.findOne(employeId);
		//Task part
		Task task=findOne(taskId);
		if(task.getAssignedEmployees()==null){
			task.setAssignedEmployees(new ArrayList<Employe>());
		}
		task.getAssignedEmployees().add(employe);
		employe.getTasks().add(task);
		//Project part
		Project project = task.getProject();
		if(project.getAssignedEmployees()==null){
			project.setAssignedEmployees(new ArrayList<Employe>());
		}
		if(!project.getAssignedEmployees().contains(employe)){
			project.getAssignedEmployees().add(employe);
			employe.getProjects().add(project);
		}
	}

	@Override
	@Transactional
	public void unAssignEmployeToTask(Long employeId, Long taskId) {
		Employe employe = employeService.findOne(employeId);
		Task task=findOne(taskId);
		if(task.getAssignedEmployees()==null){
			task.setAssignedEmployees(new ArrayList<Employe>());
		}
		task.getAssignedEmployees().remove(employe);
		employe.getTasks().remove(task);
	}
}
