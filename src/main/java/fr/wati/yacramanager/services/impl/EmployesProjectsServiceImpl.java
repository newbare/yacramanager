package fr.wati.yacramanager.services.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import fr.wati.yacramanager.beans.EmployesProjects;
import fr.wati.yacramanager.beans.EmployesProjectsId;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Project_;
import fr.wati.yacramanager.dao.repository.EmployeDto;
import fr.wati.yacramanager.dao.repository.EmployesProjectsRepository;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.EmployesProjectsService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.web.dto.EmployesProjectsDTO;
import fr.wati.yacramanager.web.dto.ProjectDTO;

@Service("employesProjectsService")
@Transactional
public class EmployesProjectsServiceImpl implements EmployesProjectsService {

	@Inject
	private EmployesProjectsRepository employesProjectsRepository;
	
	@Inject
	private EmployeService employeService;
	
	@Inject 
	private ProjectService projectService;
	
	private ApplicationEventPublisher applicationEventPublisher;
	
	public EmployesProjectsServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public <S extends EmployesProjects> S save(S entity) {
		return employesProjectsRepository.save(entity);
	}

	@Override
	public <S extends EmployesProjects> Iterable<S> save(Iterable<S> entities) {
		return employesProjectsRepository.save(entities);
	}

	@Override
	public EmployesProjects findOne(EmployesProjectsId id) {
		return employesProjectsRepository.findOne(id);
	}

	@Override
	public boolean exists(EmployesProjectsId id) {
		return employesProjectsRepository.exists(id);
	}

	@Override
	public Iterable<EmployesProjects> findAll() {
		return employesProjectsRepository.findAll();
	}

	@Override
	public Iterable<EmployesProjects> findAll(Iterable<EmployesProjectsId> ids) {
		return employesProjectsRepository.findAll(ids);
	}

	@Override
	public long count() {
		return employesProjectsRepository.count();
	}

	@Override
	public void delete(EmployesProjectsId id) {
		employesProjectsRepository.delete(id);
	}

	@Override
	public void delete(EmployesProjects entity) {
		employesProjectsRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends EmployesProjects> entities) {
		employesProjectsRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		employesProjectsRepository.deleteAll();
	}

	@Override
	public Page<EmployesProjects> findAll(Specification<EmployesProjects> spec,
			Pageable pageable) {
		Page<EmployesProjects> pageEmployesProjects= employesProjectsRepository.findAll(spec, pageable);
		//Add project that not have any assign employees
		Page<Project> pageProject = projectService.findAll(Specifications.where(new Specification<Project>() {
			@Override
			public Predicate toPredicate(Root<Project> paramRoot,
					CriteriaQuery<?> paramCriteriaQuery,
					CriteriaBuilder paramCriteriaBuilder) {
				return paramCriteriaBuilder.isEmpty(paramRoot.get(Project_.employes));
			}
		}), null);
		List<EmployesProjects> employesProjects=Lists.newArrayList(pageEmployesProjects.getContent());
		for(Project project:pageProject){
			EmployesProjects employesProjects2=new EmployesProjects();
			employesProjects2.setProject(project);
			employesProjects2.setProjectId(project.getId());
			employesProjects.add(employesProjects2);
		}
		return new PageImpl<>(employesProjects, pageable, employesProjects.size());
	}

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	@Override
	public Specification<EmployesProjects> buildSpecification(Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EmployesProjectsDTO toEmployeesProjectsDTO(
			EmployesProjects employesProjects) {
		EmployesProjectsDTO dto=new EmployesProjectsDTO();
		dto.setDailyRate(employesProjects.getDailyRate());
		dto.setId(new EmployesProjectsId(employesProjects.getEmployeeId(), employesProjects.getProjectId()));
		dto.setJoinDate(employesProjects.getJoinDate());
		dto.setLeaveDate(employesProjects.getLeaveDate());
		dto.setProjectLead(employesProjects.isProjectLead());
		if(employesProjects.getEmployee()!=null){
			EmployeDto employeDto=new EmployeDto();
			employeDto.setFirstName(employesProjects.getEmployee().getFirstName());
			employeDto.setLastName(employesProjects.getEmployee().getLastName());
			dto.setEmploye(employeDto);
		}
		ProjectDTO projectDTO=projectService.toProjectDTO(employesProjects.getProject());
		dto.setProject(projectDTO);
		return dto;
	}

	@Override
	public List<EmployesProjectsDTO> toEmployeesProjectsDTOs(
			Iterable<EmployesProjects> iterable) {
		List<EmployesProjectsDTO> employesProjectsDTOs=Lists.newArrayList(); 
		for(EmployesProjects employesProjects:iterable){
			employesProjectsDTOs.add(toEmployeesProjectsDTO(employesProjects));
		}
		return employesProjectsDTOs;
	}

	@Override
	public Specification<EmployesProjects> getGlobalSpecification(String text) {
		// TODO Auto-generated method stub
		return null;
	}

}
