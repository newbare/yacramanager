/**
 * 
 */
package fr.wati.yacramanager.web.api;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.dao.repository.EmployeDto;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.services.TaskService;
import fr.wati.yacramanager.services.impl.DtoMapper;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.api.utils.PaginationUtil;
import fr.wati.yacramanager.web.dto.ResponseWrapper;
import fr.wati.yacramanager.web.dto.TaskDTO;

/**
 * @author Rachid Ouattara
 * 
 */
@RestController
@RequestMapping("/app/api/{companyId}/task")
public class TaskRestController {

	private static final Log LOG = LogFactory.getLog(TaskRestController.class);

	@Inject
	private CompanyService companyService;

	@Inject
	private DtoMapper dtoMapper;

	@Inject
	private TaskService taskService;
	@Inject
	private EmployeService employeService;
	@Inject
	private ProjectService projectService;

	@RequestMapping(value = "/{projectId}/{employeId}", method = RequestMethod.GET)
	@Timed
	public ResponseWrapper<List<TaskDTO>> getTasks(
			@PathVariable("companyId") Long companyId,
			@PathVariable("projectId") Long projectId,
			@PathVariable("employeId") Long employeId)
			throws RestServiceException {
		Company company = companyService.findOne(companyId);
		if (company == null) {
			throw new RestServiceException("The given company doesn't exist");
		}

		Employe employe = employeService.findOne(employeId);
		if (employe == null) {
			throw new RestServiceException("The given employe doesn't exist");
		}
		if (!company.getId().equals(employe.getCompany().getId())) {
			throw new RestServiceException(
					"The given employe is not member of the given company");
		}
		Project project = projectService.findOne(projectId);
		if (project == null) {
			throw new RestServiceException("The given project doesn't exist");
		}
		List<Task> tasks = taskService.findByProjectAndAssignedEmployeesIn(
				project, employe);
		if (tasks != null && !tasks.isEmpty()) {
			ResponseWrapper<List<TaskDTO>> responseWrapper = new ResponseWrapper<>(
					taskService.toTaskDTOs(tasks), tasks.size());
			return responseWrapper;
		}
		return new ResponseWrapper<List<TaskDTO>>(null);
	}

	@RequestMapping(value = "/{taskId}/assigned/{employeId}", method = RequestMethod.GET)
	@Timed
	public ResponseWrapper<List<EmployeDto>> getAssigedEmployeesByTask(
			@PathVariable("companyId") Long companyId,
			@PathVariable("taskId") Long taskId,
			@PathVariable("employeId") Long employeId)
			throws RestServiceException {
		Company company = companyService.findOne(companyId);
		if (company == null) {
			throw new RestServiceException("The given company doesn't exist");
		}

		Employe employe = employeService.findOne(employeId);
		if (employe == null) {
			throw new RestServiceException("The given employe doesn't exist");
		}
		if (!company.getId().equals(employe.getCompany().getId())) {
			throw new RestServiceException(
					"The given employe is not member of the given company");
		}
		Task task = taskService.findOne(taskId);
		if (task == null) {
			throw new RestServiceException("The given task doesn't exist");
		}
		List<Employe> employees = employeService.getEmployeesAssignedToTask(
				employeId, taskId);
		if (employees != null && !employees.isEmpty()) {
			ResponseWrapper<List<EmployeDto>> responseWrapper = new ResponseWrapper<>(
					dtoMapper.mapEmployees(employees), employees.size());
			return responseWrapper;
		}
		return new ResponseWrapper<List<EmployeDto>>(null);
	}

	@RequestMapping(value = "/{taskId}/assign-employees", method = RequestMethod.POST)
	@Timed
	public void assignEmployeesToTask(
			@PathVariable("companyId") Long companyId,
			@PathVariable(value = "taskId") Long taskId,
			@RequestParam(value = "employeesIds", required = true) List<Long> employeesIds) throws ServiceException {
		for (Long employeeId : employeesIds) {
			taskService.assignEmployeToTask(employeeId, taskId);
		}
	}

	@RequestMapping(value = "/{taskId}/unassign-employees", method = RequestMethod.POST)
	@Timed
	public void unAssignEmployeesToTask(
			@PathVariable("companyId") Long companyId,
			@PathVariable(value = "taskId") Long taskId,
			@RequestParam(value = "employeesIds", required = true) List<Long> employeesIds) {
		for (Long employeeId : employeesIds) {
			taskService.unAssignEmployeToTask(employeeId, taskId);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{employeId}/all", method = RequestMethod.GET)
	@Timed
	@PostFilter("filterObject.getAssignedEmployeesIds().contains(#employeId) "
			+ "or  T(org.apache.commons.collections.CollectionUtils).containsAny(@employeService.getManagedEmployeesIds(#employeId),filterObject.getAssignedEmployeesIds())")
	public List<TaskDTO> getAll(
			@PathVariable("companyId") Long companyId,
			@PathVariable("employeId") @P("employeId") Long employeId,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false, value = "sort") Map<String, String> sort,
			@RequestParam(value = "filter", required = false) String filter,
			HttpServletResponse httpServletResponse)
			throws RestServiceException, URISyntaxException {
		List filters = new ArrayList<>();
		if (StringUtils.isNotEmpty(filter)) {
			try {
				filters = FilterBuilder.parse(filter);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				throw new RestServiceException(e);
			}
		}
		Specifications<Task> specifications = null;
		if (!filters.isEmpty()) {
			//LOG.debug("Building Task specification");
			specifications = Specifications.where(SpecificationBuilder
					.buildSpecification(filters, taskService));
		}
		
		Page<Task> findBySpecificationAndOrder = taskService.findAll(
				specifications, PaginationUtil.generatePageRequest(page, size, sort));
		List<TaskDTO> response = taskService
				.toTaskDTOs(findBySpecificationAndOrder);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(findBySpecificationAndOrder, "/app/api/"+companyId+"/task"+"/"+employeId+"/all", page, size);
		PaginationUtil.enhanceHttpServletResponse(headers, httpServletResponse);
		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Timed
	public ResponseEntity<TaskDTO> read(
			@PathVariable("companyId") Long companyId,
			@PathVariable("id") Long id) {
		return new ResponseEntity<TaskDTO>(taskService.toTaskDTO(taskService
				.findOne(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> update(@PathVariable("id") Long id,
			@RequestBody TaskDTO dto) {
		Task task = taskService.findOne(id);
		if (task != null) {
			dto.toTask(task);
			taskService.save(task);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>("The given task id:" + id
				+ " does not exist", HttpStatus.NOT_MODIFIED);
	}

	@RequestMapping(method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> create(@RequestBody TaskDTO dto) throws ServiceException {
		Task createTask = taskService.createTask(dto.getProjectId(),
				dto.toTask(new Task()));
		taskService.assignEmployeToTask(dto.getEmployeId(), createTask.getId());
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Timed
	@PreAuthorize("@taskService.findOne(#id) !=null &&  principal.getDomainUser().getId().equals(@taskService.toTaskDTO(@taskService.findOne(#id)).getCreatedBy())")
	public void delete(@PathVariable("companyId") Long companyId,@PathVariable("id") @P("id") Long id) {
		taskService.delete(id);
		;
	}

}
