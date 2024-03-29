package fr.wati.yacramanager.web.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.AbsencePortfolio;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.services.AbsencePortfolioService;
import fr.wati.yacramanager.services.AbsenceService;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.services.impl.DtoMapper;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.dto.AbsenceDTO;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsenceDTO;
import fr.wati.yacramanager.web.dto.ApprovalDTO;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

@RestController
@RequestMapping("/app/api/absences")
public class AbsenceController  {

	private final Logger log = LoggerFactory.getLogger(AbsenceController.class);
	@Inject
	private AbsenceService absenceService;

	@Inject
	private AbsencePortfolioService absencePortfolioService;

	@Inject
	private DtoMapper dtoMapper;

	@Inject
	private EmployeService employeService;

	@Inject
	private CompanyService companyService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	@Timed
	ResponseEntity<AbsenceDTO> read(@PathVariable("id") Long id) {
		if (absenceService.exists(id)) {
			return new ResponseEntity<AbsenceDTO>(dtoMapper.map(absenceService
					.findOne(id)), HttpStatus.OK);
		}
		return new ResponseEntity<AbsenceDTO>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Timed
	public void update(@PathVariable("id") Long id, @RequestBody AbsenceDTO dto)
			throws RestServiceException {
		Absence findOne = absenceService.findOne(id);
		try {
			absenceService.postAbsence(findOne.getEmploye().getId(),
					dto.toAbsence(findOne));
		} catch (ServiceException e) {
			log.error(e.getMessage(), e);
			throw new RestServiceException(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET)
	@Timed
	public ResponseWrapper<List<AbsenceDTO>> getAll(
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
		List filters = new ArrayList<>();
		if (StringUtils.isNotEmpty(filter)) {
			try {
				filters = FilterBuilder.parse(filter);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new RestServiceException(e);
			}
		}
		Specifications<Absence> specifications = null;
		if (!filters.isEmpty()) {
			specifications = Specifications.where(SpecificationBuilder
					.buildSpecification(filters, absenceService));
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

		Page<Absence> findByPersonne = absenceService.findAll(specifications,
				pageable);
		ResponseWrapper<List<AbsenceDTO>> responseWrapper = new ResponseWrapper<List<AbsenceDTO>>(
				dtoMapper.mapAbsences(findByPersonne),
				findByPersonne.getTotalElements());
		long startIndex = findByPersonne.getNumber() * size + 1;
		long endIndex = startIndex + findByPersonne.getNumberOfElements() - 1;
		responseWrapper.setStartIndex(startIndex);
		responseWrapper.setEndIndex(endIndex);
		return responseWrapper;
	}

	@RequestMapping(method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> create(@RequestBody AbsenceDTO dto)
			throws RestServiceException {
		Absence absence = dto.toAbsence();
		absence.setDate(new DateTime());
		absence.setEmploye(SecurityUtils.getConnectedUser());
		absence.setValidationStatus(ValidationStatus.PENDING);
		try {
			absenceService.postAbsence(
					SecurityUtils.getConnectedUser().getId(), absence);
		} catch (ServiceException e) {
			log.error(e.getMessage(), e);
			throw new RestServiceException(e.getMessage(),e);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Timed
	public void delete(@PathVariable("id") Long id) throws RestServiceException {
		if(absenceService.exists(id)){
			absenceService.delete(id);
		}else {
			throw new RestServiceException("the selected absence does not exist in DB");
		}
	}

	@RequestMapping(value = "/types", method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	List<TypeAbsenceDTO> getTypeAbsences() {
		List<TypeAbsenceDTO> absenceDTOs = new ArrayList<>();
		for (TypeAbsence absence : TypeAbsence.values()) {
			absenceDTOs.add(TypeAbsenceDTO.fromEnum(absence));
		}
		return absenceDTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.wati.yacramanager.web.api.ApprovalRestService#getApproval(java.lang
	 * .Long)
	 */
	@RequestMapping(value = "/approval", method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	ResponseWrapper<List<ApprovalDTO<AbsenceDTO>>> getApproval(
			@RequestParam(value = "requesterId") Long requesterId) {
		List<ApprovalDTO<AbsenceDTO>> approvalDTOs = new ArrayList<ApprovalDTO<AbsenceDTO>>();
		List<Absence> entitiesToApproved = absenceService
				.getEntitiesToApproved(requesterId);
		int totalCount = 0;
		Map<Employe, List<Absence>> employeAbsencesMap = new HashMap<>();
		for (Absence absence : entitiesToApproved) {
			Employe currentEmploye = absence.getEmploye();
			if (!employeAbsencesMap.containsKey(currentEmploye)) {
				employeAbsencesMap
						.put(currentEmploye, new ArrayList<Absence>());
			}
			employeAbsencesMap.get(currentEmploye).add(absence);
		}
		for (Entry<Employe, List<Absence>> entry : employeAbsencesMap
				.entrySet()) {
			ApprovalDTO<AbsenceDTO> approvalDTO = new ApprovalDTO<>();
			approvalDTO.setEmployeId(entry.getKey().getId());
			approvalDTO.setEmployeFirstName(entry.getKey().getFirstName());
			approvalDTO.setEmployeLastName(entry.getKey().getLastName());
			totalCount += entry.getValue().size();
			approvalDTO.setApprovalEntities(dtoMapper.mapAbsences(entry
					.getValue()));
			approvalDTOs.add(approvalDTO);
		}
		ResponseWrapper<List<ApprovalDTO<AbsenceDTO>>> response = new ResponseWrapper<List<ApprovalDTO<AbsenceDTO>>>(
				approvalDTOs, totalCount);
		return response;
	}

	@RequestMapping(value = "/approval/approve/{requesterId}/{entityId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	@Timed
	public void approve(@PathVariable(value = "requesterId") Long requesterId,
			@PathVariable(value = "entityId") Long entityId)
			throws RestServiceException {
		Employe employe = employeService.findOne(requesterId);
		if (employe == null) {
			throw new RestServiceException("The given employe does not exist");
		}
		Absence absence = absenceService.findOne(entityId);
		if (absence == null) {
			throw new RestServiceException(
					"The given absence id does not exist");
		}
		try {
			absenceService.validate(employe, absence);
		} catch (ServiceException e) {
			log.error(e.getMessage(), e);
			throw new RestServiceException(e.getMessage(), e);
		}
	}

	@RequestMapping(value = "/approval/reject/{requesterId}/{entityId}", method = RequestMethod.PUT)
	@Timed
	public void reject(@PathVariable(value = "requesterId") Long requesterId,
			@PathVariable(value = "entityId") Long entityId)
			throws RestServiceException {
		Employe employe = employeService.findOne(requesterId);
		if (employe == null) {
			throw new RestServiceException("The given employe does not exist");
		}
		Absence absence = absenceService.findOne(entityId);
		if (absence == null) {
			throw new RestServiceException(
					"The given absence id does not exist");
		}
		try {
			absenceService.reject(employe, absence);
		} catch (ServiceException e) {
			log.error(e.getMessage(), e);
			throw new RestServiceException(e.getMessage(), e);
		}
	}

	@RequestMapping(value = "/portfolio", method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	ResponseWrapper<List<AbsencePortfolio>> getPortfolio(
			@RequestParam(value = "requesterId") Long requesterId) {
		List<AbsencePortfolio> absencePortfolios = absencePortfolioService.findByUser(requesterId);
		for (AbsencePortfolio absencePortfolio : absencePortfolios) {
			absencePortfolio.setTypeAbsenceDTO(TypeAbsenceDTO.fromEnum(absencePortfolio.getAbsencePortfolioPK().getTypeAbsence()));
		}
		return new ResponseWrapper<List<AbsencePortfolio>>(absencePortfolios, absencePortfolios.size());

	}
}
