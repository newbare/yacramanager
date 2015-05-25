/**
 * 
 */
package fr.wati.yacramanager.web.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Attachement;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.services.AttachementService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.NoteDeFraisService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsenceDTO;
import fr.wati.yacramanager.web.dto.ApprovalDTO;
import fr.wati.yacramanager.web.dto.NoteDeFraisDTO;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

/**
 * @author Rachid Ouattara
 * 
 */
@RestController()
@RequestMapping("/app/api/frais")
public class NoteDeFraisController {

	private static final Log LOG = LogFactory
			.getLog(NoteDeFraisController.class);

	@Inject
	private NoteDeFraisService noteDeFraisService;
	
	@Inject
	private EmployeService employeService;

	@Inject
	private AttachementService attachementService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	ResponseEntity<NoteDeFraisDTO> read(@PathVariable("id") Long id) {
		if(noteDeFraisService.exists(id)){
			return new ResponseEntity<NoteDeFraisDTO>(noteDeFraisService.map(noteDeFraisService.findOne(id)), HttpStatus.OK);
		}
		return new ResponseEntity<NoteDeFraisDTO>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Timed
	public void update(@PathVariable("id") Long id,
			@RequestBody NoteDeFraisDTO dto) {
		NoteDeFrais findOne = noteDeFraisService.findOne(id);
		noteDeFraisService.save(dto.toNoteDeFrais(findOne));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET)
	@Timed
	public ResponseWrapper<List<NoteDeFraisDTO>> getExpenses(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(value = "sort", required = false) Map<String, String> sort,
			@RequestParam(value = "filter", required = false) String filter) throws RestServiceException {
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
		Specifications<NoteDeFrais> specifications = null;
		if (!filters.isEmpty()) {
			specifications = Specifications.where(SpecificationBuilder
					.buildSpecification(filters, noteDeFraisService));
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

		Page<NoteDeFrais> findBySpecificationAndOrder = noteDeFraisService
				.findAll(specifications, pageable);
		ResponseWrapper<List<NoteDeFraisDTO>> responseWrapper = new ResponseWrapper<List<NoteDeFraisDTO>>(
				noteDeFraisService.mapNoteDeFrais(findBySpecificationAndOrder),
				findBySpecificationAndOrder.getTotalElements());
		long startIndex = findBySpecificationAndOrder.getNumber() * size + 1;
		long endIndex = startIndex
				+ findBySpecificationAndOrder.getNumberOfElements() - 1;
		responseWrapper.setStartIndex(startIndex);
		responseWrapper.setEndIndex(endIndex);
		return responseWrapper;
	}

	
	@RequestMapping(value = "/{employeId}/all",method = RequestMethod.GET)
	@PostFilter("filterObject.getEmployeId().equals(#employeId)")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Timed
	public List<NoteDeFraisDTO> getAll(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(value = "sort", required = false) Map<String, String> sort,
			@RequestParam(value = "filter", required = false) String filter,
			@PathVariable("employeId") @P("employeId") Long employeId) throws RestServiceException {
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
		Specifications<NoteDeFrais> specifications = null;
		if (!filters.isEmpty()) {
			specifications = Specifications.where(SpecificationBuilder
					.buildSpecification(filters, noteDeFraisService));
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

		Page<NoteDeFrais> findBySpecificationAndOrder = noteDeFraisService
				.findAll(specifications, pageable);
		List<NoteDeFraisDTO> response = noteDeFraisService.mapNoteDeFrais(findBySpecificationAndOrder);
		return response;
	}
	@RequestMapping(method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> create(@RequestBody NoteDeFraisDTO dto) {
		try {
			dto.setValidationStatus(ValidationStatus.PENDING);
			NoteDeFrais noteDeFrais = dto.toNoteDeFrais();
			noteDeFrais.setDate(new DateTime());
			noteDeFrais.setEmploye(SecurityUtils.getConnectedUser());
			if (!dto.getAttachementsIds().isEmpty()) {
				List<Attachement> findAttachementsByIds = attachementService
						.findAttachementsByIds(dto.getAttachementsIds()
								.toArray(
										new Long[dto.getAttachementsIds()
												.size()]));
				for (Attachement attachement : findAttachementsByIds) {
					noteDeFrais.addAttachement(attachement);
					attachement.setNoteDeFrais(noteDeFrais);
					noteDeFraisService.save(noteDeFrais);
					attachementService.update(attachement);
				}
			} else {
				noteDeFraisService.save(noteDeFrais);
			}

			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception exception) {
			LOG.error(exception.getMessage(), exception);
			return new ResponseEntity<String>(exception.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Timed
	public void delete(@PathVariable("id") Long id) {
		noteDeFraisService.delete(id);
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

	@RequestMapping(value = "/approval", method = RequestMethod.GET)
	@Timed
	public @ResponseBody ResponseWrapper<List<ApprovalDTO<NoteDeFraisDTO>>> getApproval(
			@RequestParam(value="requesterId") Long requesterId) {
		List<ApprovalDTO<NoteDeFraisDTO>> approvalDTOs=new ArrayList<ApprovalDTO<NoteDeFraisDTO>>();
		List<NoteDeFrais> entitiesToApproved = noteDeFraisService.getEntitiesToApproved(requesterId);
		int totalCount=0;
		Map<Employe, List<NoteDeFrais>> employeAbsencesMap=new HashMap<>();
		for (NoteDeFrais noteDeFrais : entitiesToApproved) {
			Employe currentEmploye=noteDeFrais.getEmploye();
			if(!employeAbsencesMap.containsKey(currentEmploye)){
				employeAbsencesMap.put(currentEmploye, new ArrayList<NoteDeFrais>());
			}
			employeAbsencesMap.get(currentEmploye).add(noteDeFrais);
		}
		for(Entry<Employe, List<NoteDeFrais>> entry:employeAbsencesMap.entrySet()){
			ApprovalDTO<NoteDeFraisDTO> approvalDTO=new ApprovalDTO<>();
			approvalDTO.setEmployeId(entry.getKey().getId());
			approvalDTO.setEmployeFirstName(entry.getKey().getFirstName());
			approvalDTO.setEmployeLastName(entry.getKey().getLastName());
			totalCount+=entry.getValue().size();
			approvalDTO.setApprovalEntities(noteDeFraisService.mapNoteDeFrais(entry.getValue()));
			approvalDTOs.add(approvalDTO);
		}
		ResponseWrapper<List<ApprovalDTO<NoteDeFraisDTO>>> response=new ResponseWrapper<List<ApprovalDTO<NoteDeFraisDTO>>>(approvalDTOs,totalCount);
		return response;
	}
	
	@RequestMapping(value = "/approval/approve/{requesterId}/{entityId}", method = RequestMethod.PUT)
	@ResponseStatus(value=HttpStatus.OK)
	@Timed
	public void approve(@PathVariable(value="requesterId") Long requesterId, @PathVariable(value="entityId") Long entityId) throws RestServiceException{
		Employe employe = employeService.findOne(requesterId);
		if(employe==null){
			throw new RestServiceException("The given employe does not exist");
		}
		NoteDeFrais noteDeFrais = noteDeFraisService.findOne(entityId);
		if(noteDeFrais==null){
			throw new RestServiceException("The given expense id does not exist");
		}
		try {
			noteDeFraisService.validate(employe, noteDeFrais);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			throw new RestServiceException(e.getMessage(), e);
		}
	}

	@RequestMapping(value = "/approval/reject/{requesterId}/{entityId}", method = RequestMethod.PUT)
	@Timed
	public void reject(@PathVariable(value="requesterId") Long requesterId, @PathVariable(value="entityId") Long entityId) throws RestServiceException {
		Employe employe = employeService.findOne(requesterId);
		if(employe==null){
			throw new RestServiceException("The given employe does not exist");
		}
		NoteDeFrais noteDeFrais = noteDeFraisService.findOne(entityId);
		if(noteDeFrais==null){
			throw new RestServiceException("The given expense id does not exist");
		}
		try {
			noteDeFraisService.reject(employe, noteDeFrais);
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			throw new RestServiceException(e.getMessage(), e);
		}
	}
}
