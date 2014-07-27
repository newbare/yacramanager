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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.services.ClientService;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.utils.DtoMapper;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.dto.ProjectDTO;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

@RestController
@RequestMapping("/app/api/{companyId}/project")
public class ProjectController {

	private static final Log LOG = LogFactory.getLog(ProjectController.class);

	@Autowired
	private ProjectService projectService;
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ClientService clientService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ProjectDTO read(@PathVariable("companyId") Long companyId,@PathVariable("clientId") Long clientId,
			@PathVariable("id") Long id) {
		Client client = clientService.findOne(clientId);
		return projectService.toProjectDTO(projectService.findByClientAndId(client, id));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> update(
			@PathVariable("companyId") Long companyId,@PathVariable("clientId") Long clientId,
			@PathVariable("id") Long id, ProjectDTO dto) {
		Client client = clientService.findOne(clientId);
		Project project = projectService.findByClientAndId(client, id);
		if (project != null) {
			dto.toProject(project);
			projectService.save(project);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>("Client with id: " + id
				+ " and company id: " + companyId + " does not exist",
				HttpStatus.NOT_MODIFIED);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET)
	public ResponseWrapper<List<ProjectDTO>> getAll(
			@PathVariable("companyId") Long companyId,
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
		Specifications<Project> specifications = null;
		if (!filters.isEmpty()) {
			LOG.debug("Building Absence specification");
			specifications = Specifications.where(SpecificationBuilder
					.buildSpecification(filters, projectService));
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

		Page<Project> findBySpecificationAndOrder = projectService.findAll(specifications, pageable);
		ResponseWrapper<List<ProjectDTO>> responseWrapper = new ResponseWrapper<>(
				DtoMapper.mapProjects(findBySpecificationAndOrder),
				findBySpecificationAndOrder.getTotalElements());
		long startIndex = findBySpecificationAndOrder.getNumber() * size + 1;
		long endIndex = startIndex
				+ findBySpecificationAndOrder.getNumberOfElements() - 1;
		responseWrapper.setStartIndex(startIndex);
		responseWrapper.setEndIndex(endIndex);
		return responseWrapper;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<String> create(
			@PathVariable("companyId") Long companyId,@PathVariable("clientId") Long clientId, ProjectDTO dto) {
		projectService.createProject(clientId, dto.toProject(new Project()));
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("companyId") Long companyId,@PathVariable("clientId") Long clientId,
			@PathVariable("id") Long id) {
		Client client = clientService.findOne(clientId);
		Project project = projectService.findByClientAndId(client, id);
		if (project != null) {
			projectService.delete(project);
		}
	}

}