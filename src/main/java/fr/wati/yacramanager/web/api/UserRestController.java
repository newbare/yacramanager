package fr.wati.yacramanager.web.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.dao.repository.EmployeDto;
import fr.wati.yacramanager.dao.repository.EmployeRepository;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.impl.DtoMapper;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.dto.Navigation;
import fr.wati.yacramanager.web.dto.ResponseWrapper;
import fr.wati.yacramanager.web.dto.UserInfoDTO;
import fr.wati.yacramanager.web.dto.UserInfoDTO.ManagedEmployeInfoDTO;

@Controller
@RequestMapping(value = "/app/api/users")
public class UserRestController implements RestCrudController<EmployeDto> {

	private static final Log LOG = LogFactory.getLog(UserRestController.class);
	@Autowired
	private EmployeService employeService;
	
	@Autowired
	private DtoMapper dtoMapper;
	
	@Autowired
	private EmployeRepository employeRepository;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Timed
	public @ResponseBody
		ResponseEntity<EmployeDto> read(@PathVariable("id") Long id) {

		Employe findOne = employeRepository.findOne(id);
		if (findOne != null) {
			return new ResponseEntity<EmployeDto>(dtoMapper.map(findOne), HttpStatus.OK) ;
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Timed
	public void update(@PathVariable("id") Long id,
			@RequestBody EmployeDto employeDto) {
		Employe employe = employeRepository.findOne(id.longValue());
		employe.setFirstName(employeDto.getFirstName());
		employe.setLastName(employeDto.getLastName());
		employe.setUsername(employeDto. getUsername());
		employe.setGender(employeDto.getGender());
		employe.setBirthDay(employeDto.getBirthDay());
		employe.getContact().setEmail(employeDto.getEmail());
		employe.getContact().setPhoneNumbers(employeDto.getPhoneNumbers());
		employe.getContact().getAdresse().setPostCode(employeDto.getPostCode());
		employe.getContact().getAdresse().setAdress(employeDto.getAdress());
		employeService.save(employe);
	}

	@RequestMapping(method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> create(@RequestBody EmployeDto employeDto) {
		
		employeService.createNewEmployee(employeDto, employeDto.getCompanyId(), employeDto.getManagerId());
		return new ResponseEntity<String>(employeDto.getLastName() + " created",
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Timed
	public void delete(@PathVariable("id") Long id) {
		employeService.delete(id.longValue());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@RequestMapping(method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	ResponseWrapper<List<EmployeDto>> getAll(
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
		List filters=new ArrayList<>();
		if(StringUtils.isNotEmpty(filter)){
			try {
				filters=FilterBuilder.parse(filter);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				throw new RestServiceException(e);
			}
		}else {
			throw new RestServiceException("At least one filter should be submit");
		}
		Specifications<Employe> specifications=null;
		if(!filters.isEmpty()){
			LOG.debug("Building Absence specification");
			specifications=Specifications.where(SpecificationBuilder.buildSpecification(filters, employeService));
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
		
		Page<Employe> findBySpecificationAndOrder =employeService.findAll(specifications, pageable);
		ResponseWrapper<List<EmployeDto>> responseWrapper = new ResponseWrapper<List<EmployeDto>>(
				dtoMapper.mapEmployees(findBySpecificationAndOrder),
				findBySpecificationAndOrder.getTotalElements());
		long startIndex=findBySpecificationAndOrder.getNumber()*size+1;
		long endIndex=startIndex+findBySpecificationAndOrder.getNumberOfElements()-1;
		responseWrapper.setStartIndex(startIndex);
		responseWrapper.setEndIndex(endIndex);
		return responseWrapper;
	}

	@RequestMapping(value = "/user-info", method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	UserInfoDTO getConnectedUserInfo(HttpServletRequest request)
			throws Exception {
		try {
			Employe connectedUser = SecurityUtils.getConnectedUser();
			String contextPath = request.getContextPath();
			UserInfoDTO userInfoDTO = employeService
					.toUserInfoDTO(connectedUser.getId());
			userInfoDTO.setNavigation(Navigation.buildNavigationDefault(
					userInfoDTO, contextPath));
			return userInfoDTO;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
	}

	@RequestMapping(value = "/managed/{id}", method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	List<ManagedEmployeInfoDTO> getManagesEmploye(@PathVariable("id") Long requesterId,@RequestParam(value="me",required=false,defaultValue="false") boolean addMe) {
		List<Employe> managedEmployes= employeService.getManagedEmployees(requesterId);
		if(addMe){
			managedEmployes.add(0, SecurityUtils.getConnectedUser());
		}
		return dtoMapper.mapManagedEmployeInfoDTOs(managedEmployes);
	}

}