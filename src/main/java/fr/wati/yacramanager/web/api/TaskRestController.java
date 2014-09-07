/**
 * 
 */
package fr.wati.yacramanager.web.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.services.TaskService;
import fr.wati.yacramanager.services.impl.DtoMapper;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SpecificationBuilder;
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
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private DtoMapper dtoMapper;
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private EmployeService  employeService;
	@Autowired
	private ProjectService  projectService;
	
	@RequestMapping(value = "/{projectId}/{employeId}", method = RequestMethod.GET)
	@Timed
	public ResponseWrapper<List<TaskDTO>> getTasks(
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
		List<Task> tasks = taskService.findByProjectAndAssignedEmployeesIn(project, employe);
		if(tasks!=null && !tasks.isEmpty()){
			ResponseWrapper<List<TaskDTO>> responseWrapper = new ResponseWrapper<>(
					dtoMapper.mapTasks(tasks), tasks.size());
			return responseWrapper;
		}
		return new ResponseWrapper<List<TaskDTO>>(null);
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/{projectId}/{employeId}/all", method = RequestMethod.GET)
	@Timed
	public ResponseWrapper<List<TaskDTO>> getAll(
			@PathVariable("companyId") Long companyId,@PathVariable("projectId") Long projectId,@PathVariable("employeId") Long employeId,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false, value = "sort") Map<String, String> sort,
			@RequestParam(value = "filter", required = false) String filter)
			throws RestServiceException {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 100;
		}
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
			LOG.debug("Building Absence specification");
			specifications = Specifications.where(SpecificationBuilder
					.buildSpecification(filters, taskService));
		}
		PageRequest pageable = null;
		if (sort != null) {
			List<Order> orders = new ArrayList<>();
			for (Entry<String, String> entry : sort.entrySet()) {
				Order order = new Order(
						"asc".equals(entry.getValue()) ? Direction.ASC
								: Direction.DESC, entry.getKey());
				orders.add(order);
			}
			if (!orders.isEmpty()) {
				pageable = new PageRequest(page, size, new Sort(orders));
			} else {
				pageable = new PageRequest(page, size);
			}
		} else {
			pageable = new PageRequest(page, size);
		}

		Page<Task> findBySpecificationAndOrder = taskService.findAll(specifications, pageable);
		ResponseWrapper<List<TaskDTO>> responseWrapper = new ResponseWrapper<>(
				dtoMapper.mapTasks(findBySpecificationAndOrder),
				findBySpecificationAndOrder.getTotalElements());
		long startIndex = findBySpecificationAndOrder.getNumber() * size + 1;
		long endIndex = startIndex
				+ findBySpecificationAndOrder.getNumberOfElements() - 1;
		responseWrapper.setStartIndex(startIndex);
		responseWrapper.setEndIndex(endIndex);
		return responseWrapper;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Timed
	public ResponseEntity<TaskDTO> read(@PathVariable("companyId") Long companyId,@PathVariable("id") Long id) {
		return new ResponseEntity<TaskDTO>(dtoMapper.map(taskService.findOne(id)),HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody TaskDTO dto) {
		Task task=taskService.findOne(id);
		if (task != null) {
			dto.toTask(task);
			taskService.save(task);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>("The given task id:"+id+" does not exist",
				HttpStatus.NOT_MODIFIED);
	}

	@RequestMapping(method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> create(@RequestBody TaskDTO dto) {
		Task createTask = taskService.createTask(dto.getProjectId(), dto.toTask(new Task()));
		taskService.assignEmployeToTask(dto.getEmployeId(), createTask.getId());
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Timed
	public void delete(@PathVariable("id") Long id) {
		taskService.delete(id);;
	}
	
	
}
