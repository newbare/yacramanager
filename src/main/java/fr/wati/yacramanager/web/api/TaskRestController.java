/**
 * 
 */
package fr.wati.yacramanager.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.services.TaskService;
import fr.wati.yacramanager.utils.DtoMapper;
import fr.wati.yacramanager.web.dto.ResponseWrapper;
import fr.wati.yacramanager.web.dto.TaskDTO;

/**
 * @author Rachid Ouattara
 *
 */
@RestController
@RequestMapping("/app/api/{companyId}/task")
public class TaskRestController extends RestCrudControllerAdapter<TaskDTO> {

	@Autowired
	private CompanyService companyService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private EmployeService  employeService;
	@Autowired
	private ProjectService  projectService;
	
	@RequestMapping(value = "/{projectId}/{employeId}", method = RequestMethod.GET)
	public ResponseWrapper<List<TaskDTO>> getProjects(
			@PathVariable("companyId") Long companyId,
			@PathVariable("projectId") Long projectId,
			@PathVariable("employeId") Long employeId) throws RestServiceException{
		Company company=companyService.findOne(companyId);
		if(company==null){
			throw new RestServiceException("The given company doesn't exist");
		}
		
		Employe employe = employeService.findOne(employeId);
		if(employe==null){
			throw new RestServiceException("The given employe doesn't exist");
		}
		if(!company.getId().equals(employe.getCompany().getId())){
			throw new RestServiceException("The given employe is not member of the given company");
		}
		Project project=projectService.findOne(projectId);
		if(project==null){
			throw new RestServiceException("The given project doesn't exist");
		}
		List<Task> tasks = taskService.findByProjectAndEmploye(project, employe);
		if(tasks!=null && !tasks.isEmpty()){
			ResponseWrapper<List<TaskDTO>> responseWrapper = new ResponseWrapper<>(
					DtoMapper.mapTasks(tasks), tasks.size());
			return responseWrapper;
		}
		return new ResponseWrapper<List<TaskDTO>>(null);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.web.api.RestCrudControllerAdapter#read(java.lang.Long)
	 */
	@Override
	public TaskDTO read(Long id) {
		return super.read(id);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.web.api.RestCrudControllerAdapter#update(java.lang.Long, java.lang.Object)
	 */
	@Override
	public void update(Long id, TaskDTO dto) {
		super.update(id, dto);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.web.api.RestCrudControllerAdapter#create(java.lang.Object)
	 */
	@Override
	public ResponseEntity<String> create(TaskDTO dto) {
		return super.create(dto);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.web.api.RestCrudControllerAdapter#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		taskService.delete(id);;
	}
	
	
}
