package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Activities.ActivityOperation;
import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Project_;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.beans.TaskStatus;
import fr.wati.yacramanager.dao.repository.ClientRepository;
import fr.wati.yacramanager.dao.repository.ProjectRepository;
import fr.wati.yacramanager.dao.specifications.CommonSpecifications;
import fr.wati.yacramanager.dao.specifications.ProjectSpecification;
import fr.wati.yacramanager.listeners.ActivityEvent;
import fr.wati.yacramanager.services.ClientService;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.services.TaskService;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.utils.Filter.FilterArray;
import fr.wati.yacramanager.utils.Filter.FilterArrayValue;
import fr.wati.yacramanager.utils.Filter.FilterDate;
import fr.wati.yacramanager.utils.Filter.FilterText;
import fr.wati.yacramanager.utils.Filter.FilterType;
import fr.wati.yacramanager.web.api.ClientController;
import fr.wati.yacramanager.web.dto.ProjectDTO;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{

	private static final Log LOG = LogFactory.getLog(ProjectServiceImpl.class);
	
	@Inject
	private ProjectRepository projectRepository;
	@Inject
	private ClientRepository clientRepository;
	@Inject
	private ClientService  clientService;
	
	@Inject
	private DtoMapper dtoMapper;
	
	@Inject
	private CompanyService companyService;
	
//	@Autowired
//	private RabbitTemplate rabbitTemplate;
	
	@Inject
	private TaskService taskService;
	private ApplicationEventPublisher applicationEventPublisher;

	public Project createProject(Long clientId,Project project){
		Client client=clientRepository.findOne(clientId);
		project.setClient(client);
		project.setCreatedDate(new DateTime());
		Project saveProject = projectRepository.save(project);
		client.getProjects().add(project);
		/*
		 * Each project should have at least one default Task 
		 */
		Task defaulTask=new Task();
		defaulTask.setCreatedDate(new DateTime());
		defaulTask.setName("[Default task]");
		defaulTask.setColor(project.getColor());
		defaulTask.setTaskStatus(TaskStatus.OPEN);
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
		S save = projectRepository.save(entity);
		applicationEventPublisher.publishEvent(ActivityEvent
				.createWithSource(this).user()
				.operation(ActivityOperation.CREATE)
				.onEntity(Project.class, save.getId()));
		return save;
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
		//LOG.debug(rabbitTemplate);
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

	@Override
	public Page<Project> findAll(Specification<Project> spec, Pageable pageable) {
		return projectRepository.findAll(spec, pageable);
	}

	@Override
	public Specification buildSpecification(Filter filter) {
		if(filter!=null){
			FilterType filterType = filter.getType();
			switch (filterType) {
			case ARRAY:
				FilterArray filterArray=(FilterArray) filter;
				if("company".equals(filter.getField())){
					return ProjectSpecification.findForCompany(Long.valueOf(filterArray.getValue().get(0).getName()));
				}
				if("client".equals(filter.getField())){
					List<Client> clients=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						clients.add(clientService.findOne(Long.valueOf(filterArrayValue.getName())));
					}
					return CommonSpecifications.equalsAny(clients, Project_.client);
				}
				break;
			case TEXT:
				FilterText filterText=(FilterText) filter;
				if("description".equals(filterText.getField())){
					return CommonSpecifications.likeIgnoreCase(filterText.getValue(), Project_.description);
				}
				if("name".equals(filterText.getField())){
					return CommonSpecifications.likeIgnoreCase(filterText.getValue(), Project_.name);
				}
				break;
			case DATE:
			case DATE_RANGE:
				FilterDate filterDate=(FilterDate) filter;
				if("createdDate".equals(filter.getField())){
					if(filterDate.isRangedDate()){
						return CommonSpecifications.betweenDate(filterDate.getValue().getStart(), filterDate.getValue().getEnd(), Project.class,"createdDate");
					}else {
						return CommonSpecifications.equals(filterDate.getValue().getDate(), Project_.createdDate);
					}
				}
				break;
			default:
				break;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.ProjectService#toProjectDTO(fr.wati.yacramanager.beans.Project)
	 */
	@Override
	@Transactional
	public ProjectDTO toProjectDTO(Project project) {
		ProjectDTO dto=new ProjectDTO();
		project=findOne(project.getId());
		dto.setId(project.getId());
		dto.setCreatedDate(project.getCreatedDate());
		dto.setName(project.getName());
		dto.setDescription(project.getDescription());
		dto.setTasks(dtoMapper.mapTasks(project.getTasks()));
		dto.setClient(clientService.toClientDTO(project.getClient()));
		dto.setNumberOfEmployes(project.getAssignedEmployees().size());
		dto.setNumberOfTasks(project.getTasks().size());
		return dto;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.ProjectService#findByClientAndId(fr.wati.yacramanager.beans.Client, java.lang.Long)
	 */
	@Override
	public Project findByClientAndId(Client client, Long id) {
		return projectRepository.findByClientAndId(client, id);
	}

	@Override
	public List<Project> findByAssignedEmployeesIn(Employe employe) {
		return projectRepository.findByAssignedEmployeesIn(employe);
	}
	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	@Override
	public long countNumberOfEmployeeForProject(Project project) {
		return project.getAssignedEmployees().size();
	}

	@Override
	public long countNumberOfTaskForProject(Project project) {
		return project.getTasks().size();
	}
}
