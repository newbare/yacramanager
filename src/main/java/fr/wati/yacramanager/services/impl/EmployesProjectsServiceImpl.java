package fr.wati.yacramanager.services.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import fr.wati.yacramanager.beans.EmployesProjects;
import fr.wati.yacramanager.beans.EmployesProjectsId;
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
		return employesProjectsRepository.findAll(spec, pageable);
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
		//TODO complete mapping
		
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

}
