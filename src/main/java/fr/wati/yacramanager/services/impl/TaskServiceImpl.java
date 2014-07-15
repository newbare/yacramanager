package fr.wati.yacramanager.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.dao.ProjectRepository;
import fr.wati.yacramanager.dao.TaskRepository;
import fr.wati.yacramanager.services.TaskService;

@Transactional
@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ProjectRepository  projectRepository;
	
	@Override
	public <S extends Task> S save(S entity) {
		return taskRepository.save(entity);
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
		Task saveTask = taskRepository.save(task);
		project.getTasks().add(saveTask);
		return saveTask;
	}

	@Override
	public void deleteTask(Long projectId, Long taskId) {
		Project project=projectRepository.findOne(projectId);
		Task task = taskRepository.findByProjectAndId(project, taskId);
		taskRepository.delete(task);
	}

}
