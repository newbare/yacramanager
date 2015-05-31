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
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Client_;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Employe_;
import fr.wati.yacramanager.beans.Project_;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.beans.Task_;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.beans.WorkLog;
import fr.wati.yacramanager.beans.WorkLog_;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.services.TaskService;
import fr.wati.yacramanager.services.WorkLogService;
import fr.wati.yacramanager.services.impl.DtoMapper;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.dto.ResponseWrapper;
import fr.wati.yacramanager.web.dto.WorkLogDTO;

@RestController
@RequestMapping(value = "/app/api/worklog")
public class WorkLogRestController {

	private static final Log LOG=LogFactory.getLog(WorkLogRestController.class); 
	
	@Inject
	private WorkLogService workLogService;
	
	@Inject
	private DtoMapper dtoMapper;
	
	@Inject
	private EmployeService employeService;
	@Inject
	private TaskService taskService;

	@RequestMapping(value="/calendar",method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	ResponseEntity<List<WorkLogDTO>> getEvents(
			@RequestParam(value = "start", required = true) long start,
			@RequestParam(value = "end", required = true) long end) {
		// We receive time in second
		LocalDateTime startDate = new LocalDateTime(start * 1000);
		LocalDateTime endDate = new LocalDateTime(end * 1000);
		List<WorkLog> workLogs = workLogService.findByEmployeAndStartDateBetween(SecurityUtils.getConnectedUser(), startDate, endDate);
		if(workLogs!=null && !workLogs.isEmpty()){
			List<WorkLogDTO> dtos = dtoMapper.mapWorkLogs(workLogs);
			return new ResponseEntity<List<WorkLogDTO>>(dtos, HttpStatus.OK);
		}
		return new ResponseEntity<List<WorkLogDTO>>(HttpStatus.OK);
	}


	@RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
	@Timed
	public ResponseEntity<WorkLogDTO> getEventDetails(@PathVariable("id") Long id){
		WorkLog workLog = workLogService.findOne(id);
		if(workLog!=null){
			WorkLogDTO workLogDTO=dtoMapper.mapForDetails(workLog);
			return new ResponseEntity<WorkLogDTO>(workLogDTO, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	ResponseEntity<WorkLogDTO> read(@PathVariable("id") Long id) {
		if(workLogService.exists(id)){
			return new ResponseEntity<WorkLogDTO>(dtoMapper.map(workLogService.findOne(id)), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Timed
	public void update(@PathVariable("id") Long id, @RequestBody WorkLogDTO dto) throws RestServiceException {
		WorkLog findOne = workLogService.findOne(id);
		try {
			workLogService.updateWorkLog(dto.toWorkLog(findOne));
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			throw new RestServiceException(e);
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET)
	@Timed
	public @ResponseBody ResponseWrapper<List<WorkLogDTO>> getAll(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(value = "sort", required = false) Map<String, String> sort,
			@RequestParam(value = "filter", required = false) String filter)
			throws RestServiceException {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 100;
		}
		List filters=new ArrayList<>();
		if(StringUtils.isNotEmpty(filter)){
			try {
				filters=FilterBuilder.parse(filter);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				throw new RestServiceException(e);
			}
		}
		Specifications<WorkLog> specifications=null;
		if(!filters.isEmpty()){
			LOG.debug("Building Absence specification");
			specifications=Specifications.where(SpecificationBuilder.buildSpecification(filters, workLogService));
		}
		PageRequest pageable=null;
		if(sort!=null){
			List<Order> orders=new ArrayList<>();
			for(Entry<String, String> entry:sort.entrySet()){
				Order order=new Order("asc".equals(entry.getValue())?Direction.ASC:Direction.DESC, entry.getKey());
				orders.add(order);
			}
			if(!orders.isEmpty()){
				pageable=new PageRequest(page, size, new Sort(orders));
			}else {
				pageable=new PageRequest(page, size);
			}
		}else {
			pageable=new PageRequest(page, size);
		}
		
		Page<WorkLog> findBySpecification =workLogService.findAll(specifications, pageable);
		ResponseWrapper<List<WorkLogDTO>> responseWrapper = new ResponseWrapper<List<WorkLogDTO>>(
				dtoMapper.mapWorkLogs(findBySpecification),
				findBySpecification.getTotalElements());
		long startIndex=findBySpecification.getNumber()*size+1;
		long endIndex=startIndex+findBySpecification.getNumberOfElements()-1;
		responseWrapper.setStartIndex(startIndex);
		responseWrapper.setEndIndex(endIndex);
		return responseWrapper;
	}


	@RequestMapping(method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> create(@RequestBody WorkLogDTO dto) throws RestServiceException{
		if(dto.getTaskId()==null){
			throw new RestServiceException("No given task ID error");
		}
		dto.setValidationStatus(ValidationStatus.PENDING);
		WorkLog workLog = dto.toWorkLog();
		workLog.setCreatedDate(new DateTime());
		if(dto.getEmployeId()==null){
			workLog.setEmploye(SecurityUtils.getConnectedUser());
		}else {
			Employe employe=employeService.findOne(dto.getEmployeId());
			workLog.setEmploye(employe);
		}
		Task task=taskService.findOne(dto.getTaskId());
		workLog.setTask(task);
		try {
			workLogService.postWorkLog(workLog);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			throw new RestServiceException(e);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Timed
	public void delete(@PathVariable("id") Long id) {
		workLogService.delete(id);
	}
	
	@RequestMapping(value = "/client/{clientId}", method = RequestMethod.GET)
	@PreAuthorize("@clientService.findOne(#clientId).getCompany().getId().equals(principal.getDomainUser().getCompany().getId())")
	@Timed
	public Long clientWorkLog(
			@PathVariable("clientId") @P("clientId") final Long clientId,
			@RequestParam(value="employeId",required=false) final Long employeId,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(iso = ISO.DATE) final LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(iso = ISO.DATE) final LocalDate endDate) {
		Specifications<WorkLog> specifications = Specifications
				.where(new Specification<WorkLog>() {
					public Predicate toPredicate(Root<WorkLog> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						return cb.equal(
								root.join(WorkLog_.task).join(Task_.project)
										.join(Project_.client).get(Client_.id),
								clientId);
					}
				});
		if(startDate!=null && endDate!=null){
			specifications=specifications.and(new Specification<WorkLog>() {
				public Predicate toPredicate(Root<WorkLog> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.between(root.get(WorkLog_.startDate), startDate, endDate);
				}
			})
			.or(new Specification<WorkLog>() {
				public Predicate toPredicate(Root<WorkLog> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.between(root.get(WorkLog_.endDate), startDate, endDate);
				}
			});
		}
		if(employeId!=null){
			specifications=specifications.and(new Specification<WorkLog>() {
				public Predicate toPredicate(Root<WorkLog> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.join(WorkLog_.employe).get(Employe_.id), employeId);
				}
			});
		}
		Page<WorkLog> workLogs = workLogService.findAll(specifications, null);
		Long totalDuration=0L;
		for(WorkLog workLog:workLogs){
			totalDuration+=workLog.calculateDuration();
		}
		return totalDuration;
	}
	
	@RequestMapping(value = "/project/{projectId}", method = RequestMethod.GET)
	@PreAuthorize("@projectService.findOne(#projectId).getClient().getCompany().getId().equals(principal.getDomainUser().getCompany().getId())")
	@Timed
	public Long projectWorkLog(
			@PathVariable("projectId") @P("projectId") final Long projectId,
			@RequestParam(value="employeId",required=false) final Long employeId,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(iso = ISO.DATE) final LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(iso = ISO.DATE) final LocalDate endDate) {
		Specifications<WorkLog> specifications = Specifications
				.where(new Specification<WorkLog>() {
					public Predicate toPredicate(Root<WorkLog> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						return cb.equal(root.join(WorkLog_.task).join(Task_.project).get(Project_.id),projectId);
					}
				});
		if(startDate!=null && endDate!=null){
			specifications=specifications.and(new Specification<WorkLog>() {
				public Predicate toPredicate(Root<WorkLog> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.between(root.get(WorkLog_.startDate), startDate, endDate);
				}
			})
			.or(new Specification<WorkLog>() {
				public Predicate toPredicate(Root<WorkLog> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.between(root.get(WorkLog_.endDate), startDate, endDate);
				}
			});
		}
		if(employeId!=null){
			specifications=specifications.and(new Specification<WorkLog>() {
				public Predicate toPredicate(Root<WorkLog> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.join(WorkLog_.employe).get(Employe_.id), employeId);
				}
			});
		}
		Page<WorkLog> workLogs = workLogService.findAll(specifications, null);
		Long totalDuration=0L;
		for(WorkLog workLog:workLogs){
			totalDuration+=workLog.calculateDuration();
		}
		return totalDuration;
	}
	
	@RequestMapping(value = "/task/{taskId}", method = RequestMethod.GET)
	@PreAuthorize("@taskService.findOne(#taskId).getProject().getClient().getCompany().getId().equals(principal.getDomainUser().getCompany().getId())")
	@Timed
	public Long taskWorkLog(
			@PathVariable("taskId") @P("taskId") final Long taskId,
			@RequestParam(value="employeId",required=false) final Long employeId,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(iso = ISO.DATE) final LocalDate startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(iso = ISO.DATE) final LocalDate endDate) {
		Specifications<WorkLog> specifications = Specifications
				.where(new Specification<WorkLog>() {
					public Predicate toPredicate(Root<WorkLog> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						return cb.equal(
								root.join(WorkLog_.task).get(Task_.id),	taskId);
					}
				});
		if(startDate!=null && endDate!=null){
			specifications=specifications.and(new Specification<WorkLog>() {
				public Predicate toPredicate(Root<WorkLog> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.between(root.get(WorkLog_.startDate), startDate, endDate);
				}
			})
			.or(new Specification<WorkLog>() {
				public Predicate toPredicate(Root<WorkLog> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.between(root.get(WorkLog_.endDate), startDate, endDate);
				}
			});
		}
		if(employeId!=null){
			specifications=specifications.and(new Specification<WorkLog>() {
				public Predicate toPredicate(Root<WorkLog> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.join(WorkLog_.employe).get(Employe_.id), employeId);
				}
			});
		}
		Page<WorkLog> workLogs = workLogService.findAll(specifications, null);
		Long totalDuration=0L;
		for(WorkLog workLog:workLogs){
			totalDuration+=workLog.calculateDuration();
		}
		return totalDuration;
	}
	
}
