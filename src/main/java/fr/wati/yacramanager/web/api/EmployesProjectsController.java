package fr.wati.yacramanager.web.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
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

import fr.wati.yacramanager.beans.Client_;
import fr.wati.yacramanager.beans.EmployesProjects;
import fr.wati.yacramanager.beans.EmployesProjectsId;
import fr.wati.yacramanager.beans.EmployesProjects_;
import fr.wati.yacramanager.beans.Project_;
import fr.wati.yacramanager.dao.specifications.CommonSpecifications;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.EmployesProjectsService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.ResourceNotFoundException;
import fr.wati.yacramanager.web.dto.EmployesProjectsDTO;

@RestController
@RequestMapping("/app/api/{companyId}/{clientId}/employesprojects")
public class EmployesProjectsController {

	private static final Log LOG = LogFactory.getLog(EmployesProjectsController.class);

	@Inject
	private ProjectService projectService;
	@Inject
	private EmployesProjectsService employesProjectsService;
	
	@Inject
	private EmployeService  employeService;

	@RequestMapping(value = "/{projectId}/{employeeId}", method = RequestMethod.GET)
	public EmployesProjectsDTO read(@PathVariable("companyId") Long companyId,@PathVariable("projectId") Long projectId,@PathVariable("employeeId") Long employeeId) {
		EmployesProjects employesProjects = employesProjectsService.findOne(new EmployesProjectsId(employeeId, projectId));
		if(employesProjects==null){
			throw new ResourceNotFoundException("The project does not exist");
		}
		return employesProjectsService.toEmployeesProjectsDTO(employesProjects);
	}

	@RequestMapping(value = "/{projectId}/{employeeId}", method = RequestMethod.PUT)
	public ResponseEntity<String> update(
			@PathVariable("companyId") Long companyId,@PathVariable("id") Long id,@PathVariable("projectId") Long projectId,@PathVariable("employeeId") Long employeeId, @RequestBody EmployesProjectsDTO dto) {
		EmployesProjects employesProjects = employesProjectsService.findOne(new EmployesProjectsId(employeeId, projectId));
		if (employesProjects != null) {
			employesProjectsService.save(dto.toEmployeesProjects(employesProjects));
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>("Client with id: " + id
				+ " and company id: " + companyId + " does not exist",
				HttpStatus.NOT_MODIFIED);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET)
	public List<EmployesProjectsDTO> getAll(
			@PathVariable("companyId") Long companyId,
			@PathVariable("clientId") final Long clientId,
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
		Specifications<EmployesProjects> specifications = null;
		if (!filters.isEmpty()) {
			specifications = Specifications.where(SpecificationBuilder
					.buildSpecification(filters, employesProjectsService));
		}
		//Force filter on client id
		specifications=Specifications.where(new Specification<EmployesProjects>() {
			public Predicate toPredicate(Root<EmployesProjects> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.equal(root.join(EmployesProjects_.project).join(Project_.client).get(Client_.id),clientId);
			}
		}).and(specifications);
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

		Page<EmployesProjects> findBySpecificationAndOrder = employesProjectsService.findAll(specifications, pageable);
		List<EmployesProjectsDTO> employesProjectsDTOs = employesProjectsService.toEmployeesProjectsDTOs(findBySpecificationAndOrder);
		return employesProjectsDTOs;
	}

	@RequestMapping(value = "/{projectId}/{employeeId}", method = RequestMethod.DELETE)
	@Timed
	public void delete(@PathVariable("companyId") Long companyId,@PathVariable("id") Long id,@PathVariable("projectId") Long projectId,@PathVariable("employeeId") Long employeeId) {
		EmployesProjects employesProjects = employesProjectsService.findOne(new EmployesProjectsId(employeeId, projectId));
		if (employesProjects != null) {
			employesProjectsService.delete(employesProjects);
		}
	}
	
}
