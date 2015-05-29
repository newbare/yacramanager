package fr.wati.yacramanager.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import fr.wati.yacramanager.beans.Activities.ActivityOperation;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.EmployesProjects;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.beans.TaskStatus;
import fr.wati.yacramanager.beans.Task_;
import fr.wati.yacramanager.dao.repository.ProjectRepository;
import fr.wati.yacramanager.dao.repository.TaskRepository;
import fr.wati.yacramanager.dao.specifications.CommonSpecifications;
import fr.wati.yacramanager.listeners.ActivityEvent;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.services.TaskService;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.utils.Filter.FilterArray;
import fr.wati.yacramanager.utils.Filter.FilterArrayValue;
import fr.wati.yacramanager.utils.Filter.FilterText;
import fr.wati.yacramanager.utils.Filter.FilterType;
import fr.wati.yacramanager.web.dto.TaskDTO;

@Transactional
@Service("taskService")
public class TaskServiceImpl implements TaskService {

	@Inject
	private TaskRepository taskRepository;
	@Inject
	private ProjectRepository  projectRepository;
	
	@Inject
	private ProjectService projectService;
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
				.onEntity(Task.class, save.getId()).dto(toTaskDTO(save)));
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
	@Transactional
	public void delete(Long id) {
		Task task=findOne(id);
		if(task.getAssignedEmployees()!=null && !task.getAssignedEmployees().isEmpty()){
			for(Employe employe:task.getAssignedEmployees()){
				employe.getTasks().remove(task);
			}
			task.getAssignedEmployees().clear();
		}
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
		if(filter!=null){
			FilterType filterType = filter.getType();
			switch (filterType) {
			case ARRAY:
				FilterArray filterArray=(FilterArray) filter;
				if("taskStatus".equals(filter.getField())){
					List<TaskStatus> taskStatus=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						taskStatus.add(TaskStatus.valueOf(filterArrayValue.getName()));
					}
					return CommonSpecifications.equalsAny(taskStatus, Task_.taskStatus);
				}
				if("project".equals(filter.getField())){
					List<Project> projects=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						projects.add(projectService.findOne(Long.valueOf(filterArrayValue.getName())));
					}
					return CommonSpecifications.equalsAny(projects, Task_.project);
				}
				if("employe".equals(filter.getField())){
					List<Employe> employes=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						employes.add(employeService.findOne(Long.valueOf(filterArrayValue.getName())));
					}
					Specifications<Task> specifications=null;
					for(Employe employe:employes){
						if(specifications==null){
							specifications=Specifications.where(CommonSpecifications.isMember(employe, Task_.assignedEmployees));
						}else {
							specifications =specifications.or(CommonSpecifications.isMember(employe, Task_.assignedEmployees));
						}
					}
					return specifications;
				}
				break;
				case TEXT:
					FilterText filterText=(FilterText) filter;
					if("name".equals(filterText.getField())){
						return CommonSpecifications.likeIgnoreCase(filterText.getValue(), Task_.name);
					}
					if("description".equals(filterText.getField())){
						return CommonSpecifications.likeIgnoreCase(filterText.getValue(), Task_.description);
					}
					break;
			default:
				break;
			}
		}
		return null;
	}

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	@Override
	@Transactional
	public void assignEmployeToTask(Long employeId, Long taskId) throws ServiceException {
		final Employe employe = employeService.findOne(employeId);
		//Task part
		Task task=findOne(taskId);
		if(task.getAssignedEmployees()==null){
			task.setAssignedEmployees(new ArrayList<Employe>());
		}
		task.getAssignedEmployees().add(employe);
		employe.getTasks().add(task);
//		//Project part
		Project project = task.getProject();
		int countMatches = CollectionUtils.countMatches(project.getEmployes(), new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				EmployesProjects employesProjects=(EmployesProjects) object;
				return employesProjects.getEmployee().equals(employe);
			}
		});
		if(countMatches==0){
			projectService.assignEmployeToProject(project.getId(), employe.getId(), false, BigDecimal.ZERO);
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

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public TaskDTO toTaskDTO(Task task) {
		TaskDTO dto=new TaskDTO();
		task=findOne(task.getId());
		dto.setCreatedDate(task.getCreatedDate());
		dto.setLastModifiedDate(task.getLastModifiedDate());
		dto.setDescription(task.getDescription());
		dto.setColor(task.getColor());
		if(task.getCreatedBy()!=null){
			dto.setCreatedBy(task.getCreatedBy().getId());
		}
		List<Employe> assignedEmployees=Lists.newArrayList();
		assignedEmployees.addAll(task.getAssignedEmployees());
		List<Long> assignedEmployeesIds = (List<Long>) CollectionUtils.collect(assignedEmployees, new Transformer() {
			@Override
			public Object transform(Object input) {
				Employe employe=(Employe) input;
				return (Long)employe.getId();
			}
		});
		dto.setAssignedEmployeesIds(assignedEmployeesIds);
		dto.setId(task.getId());
		dto.setName(task.getName());
		dto.setTaskStatus(task.getTaskStatus());
		dto.setProjectId(task.getProject().getId());
		dto.setProject(projectService.toProjectDTO(task.getProject()));
		return dto;
	}

	@Override
	@Transactional
	public List<TaskDTO> toTaskDTOs(Iterable<Task> tasks) {
		List<TaskDTO> dtos=Lists.newArrayList();
		for(Task task:tasks){
			dtos.add(toTaskDTO(task));
		}
		return dtos;
	}
}
