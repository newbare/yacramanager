package fr.wati.yacramanager.services.impl;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.GlobalSearchService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.web.dto.SearchResult;

@Service
@Transactional
public class GlobalSearchServiceImpl implements GlobalSearchService {

	@Inject
	private EmployeService employeService;
	
	@Inject
	private ProjectService projectService;
	
	@Inject
	private DtoMapper dtoMapper;
	
	public GlobalSearchServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public SearchResult search(String textTosearch) {
		SearchResult searchResult=new SearchResult();
		Page<Employe> employeeResults = employeService.findAll(employeService.getGlobalSpecification(textTosearch),null);
		searchResult.getResult().get("employe").addAll((Collection) dtoMapper.mapEmployees(employeeResults));
		searchResult.setResultCount(searchResult.getResultCount()+employeeResults.getTotalElements());
		Page<Project> projectResult = projectService.findAll(projectService.getGlobalSpecification(textTosearch),null);
		searchResult.getResult().get("project").addAll((Collection) dtoMapper.mapProjects(projectResult));
		searchResult.setResultCount(searchResult.getResultCount()+projectResult.getTotalElements());
		searchResult.setTextToSearch(textTosearch);
		return searchResult;
	}

}
