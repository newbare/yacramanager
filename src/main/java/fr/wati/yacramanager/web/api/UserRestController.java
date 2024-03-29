package fr.wati.yacramanager.web.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Company_;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Employe_;
import fr.wati.yacramanager.beans.Role;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.dao.repository.EmployeDto;
import fr.wati.yacramanager.dao.repository.EmployeRepository;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.services.UserService;
import fr.wati.yacramanager.services.impl.DtoMapper;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.dto.ResponseWrapper;
import fr.wati.yacramanager.web.dto.UserInfoDTO;
import fr.wati.yacramanager.web.dto.UserInfoDTO.ManagedEmployeInfoDTO;

@RestController
@RequestMapping(value = "/app/api/users")
public class UserRestController {

	private static final Log LOG = LogFactory.getLog(UserRestController.class);
	@Inject
	private EmployeService employeService;
	
	@Inject
	private UserService userService;
	
	@Inject
	private DtoMapper dtoMapper;
	
	@Inject
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
		employe.setUserName(employeDto. getEmail());
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
	@RolesAllowed(Role.SSII_ADMIN)
	public ResponseEntity<String> create(@RequestBody EmployeDto employeDto) {
		
		employeService.createNewEmployee(employeDto, employeDto.getCompanyId(), employeDto.getManagerId());
		return new ResponseEntity<String>(employeDto.getLastName() + " created",
				HttpStatus.CREATED);
	}

	/**
     * POST  /rest/change-password -> changes the current user's password
     */
    @RequestMapping(value = "/change-password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody String password) {
    	if (StringUtils.isEmpty(password)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.changePassword(SecurityUtils.getConnectedUser().getId(),password);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/update-manager/{employeeId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
	@RolesAllowed(Role.SSII_ADMIN)
    public ResponseEntity<?> updateManager(@PathVariable("employeeId") Long employeeId ,@RequestBody(required=true) Long managerId) throws RestServiceException {
    	try {
			employeService.updateManager(employeeId,managerId);
		} catch (ServiceException e) {
			LOG.error(e);
			throw new RestServiceException(e);
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/update-rights/{employeeId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
	@RolesAllowed({Role.SSII_ADMIN,Role.ADMIN})
    public ResponseEntity<?> updateUserRights(@PathVariable("employeeId") Long employeeId ,@RequestBody(required=true) List<String> roles) throws RestServiceException {
    	try {
			employeService.updateUserRights(employeeId,roles);
		} catch (ServiceException e) {
			LOG.error(e);
			throw new RestServiceException(e);
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Timed
	public void delete(@PathVariable("id") Long id) {
		employeService.delete(id.longValue());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		}
		Specifications<Employe> specifications=null;
		if(!filters.isEmpty()){
			specifications=Specifications.where(SpecificationBuilder.buildSpecification(filters, employeService));
		}
		//Force filter on company id
		specifications=Specifications.where(new Specification<Employe>() {
			public Predicate toPredicate(Root<Employe> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.equal(root.join(Employe_.company).get(Company_.id),SecurityUtils.getConnectedUser().getCompany().getId());
			}
		}).and(specifications);
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
			UserInfoDTO userInfoDTO = employeService
					.toUserInfoDTO(connectedUser.getId());
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

	@RequestMapping(value = "/avatar/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<InputStreamResource> downloadUserAvatarImage(
			@PathVariable Long userId, HttpServletRequest httpServletRequest) {
		Employe employe = employeService.findOne(userId);
		try {
			if (employe != null && userId != 0) {
				if (employe.getAvatar()!=null) {
					byte[] userAvatar = userService.getAvatar(userId);
					if(userAvatar!=null){
						return ResponseEntity
								.ok()
								.contentType(MediaType.IMAGE_JPEG)
								.body(new InputStreamResource(
										new ByteArrayInputStream(userService.getAvatar(userId))));
					}
				} else if(employe.isSocialUser()){

					byte[] profileImage = IOUtils.toByteArray(new URI(employe
							.getProfileImageUrl()));
					return ResponseEntity
							.ok()
							.contentType(MediaType.IMAGE_JPEG)
							.body(new InputStreamResource(
									new ByteArrayInputStream(profileImage)));

				}
			}
		} catch (IOException | URISyntaxException e) {
			LOG.error(e.getMessage(), e);
		}
		InputStream defaultAvatar = httpServletRequest.getServletContext()
				.getResourceAsStream("/assets/images/users/no_user_pic.jpg");
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
				.body(new InputStreamResource(defaultAvatar));
	}
	
	@RequestMapping(value="/avatar/{userId}",method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> updateAvatar(@PathVariable("userId") Long userId, @RequestParam("file") MultipartFile file) {
		try {
			if (file != null) {
				Users user = userService.findOne(userId);
				user.setAvatar(IOUtils.toByteArray(file
						.getInputStream()));
				userService.save(user);
				return new ResponseEntity<>( HttpStatus.OK);
			}
			return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
		} catch (Exception exception) {
			LOG.error(exception.getMessage(), exception);
			return new ResponseEntity<String>(exception.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}