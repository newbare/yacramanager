package fr.wati.yacramanager.web.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.dao.EmployeDto;
import fr.wati.yacramanager.dao.EmployeRepository;
import fr.wati.yacramanager.services.impl.EmployeServiceImpl;
import fr.wati.yacramanager.utils.DtoMapper;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.web.dto.Navigation;
import fr.wati.yacramanager.web.dto.ResponseWrapper;
import fr.wati.yacramanager.web.dto.UserInfoDTO;

@Controller
@RequestMapping(value = "/app/api/users")
public class UserRestController implements RestCrudController<EmployeDto>{

	private static final Log LOG=LogFactory.getLog(UserRestController.class);
	@Autowired
	private EmployeServiceImpl employeService;
	@Autowired
	private EmployeRepository employeRepository;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public @ResponseBody EmployeDto read(@PathVariable("id") Long id) {
	  
	Employe findOne = employeRepository.findOne(id);
	if(findOne!=null){
		return DtoMapper.map(findOne);
	}
    return null;
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(@PathVariable("id") Long id, @RequestBody EmployeDto employeDto) {
	  Employe employe = employeRepository.findOne(id.longValue());
	  employe.setPrenom(employeDto.getPrenom());
	  employe.setNom(employeDto.getNom());
	  employe.setUsername(employeDto.getUsername());
	  employe.setCivilite(employeDto.getCivilite());
	  employe.setDateNaissance(employeDto.getDateNaissance());
	  employe.getContact().setEmail(employeDto.getEmail());
	  employeService.save(employe);
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<String> create(@RequestBody EmployeDto employeDto) {
	  Employe employe=new Employe();
	  employe.setPrenom(employeDto.getPrenom());
	  employe.setNom(employeDto.getNom());
	  employe.setUsername(employeDto.getUsername());
	  employe.setPassword(employeDto.getPassword());
	  employe.setCivilite(employeDto.getCivilite());
	  employe.setDateNaissance(employeDto.getDateNaissance());
	  employe.getContact().setEmail(employeDto.getEmail());
	  employeService.save(employe);
	  return new ResponseEntity<String>(employeDto.getNom()+" created", HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id) {
	  employeService.delete(id.longValue());
  }

@Override
@RequestMapping(method=RequestMethod.GET)
public @ResponseBody ResponseWrapper<List<EmployeDto>> getAll(@RequestParam(required=false) Integer page,@RequestParam(required=false) Integer size,@RequestParam(value="sort", required=false) Map<String, String> sort,@RequestParam(value="filter", required=false) Map<String, String> filter) {
	if(page==null){
		page=0;
	}
	if(size==null){
		size=100;
	}
	PageRequest pageable=null;
	if(sort!=null){
		List<Order> orders=new ArrayList<>();
		for(Entry<String, String> entry:sort.entrySet()){
			Order order=new Order("asc".equals(entry.getValue())?Direction.ASC:Direction.DESC, entry.getKey());
			orders.add(order);
		}
		pageable=new PageRequest(page, size, new Sort(orders));
	}else {
		pageable=new PageRequest(page, size);
	}
	Page<Employe> all = employeRepository.findAll(pageable);
	return new ResponseWrapper<List<EmployeDto>>(DtoMapper.mapEmployees(all),all.getTotalElements());
}

@RequestMapping(value="/user-info", method=RequestMethod.GET)
public @ResponseBody UserInfoDTO getConnectedUserInfo(HttpServletRequest request) throws Exception{
	try {
		String contextPath=request.getContextPath();
		 UserInfoDTO userInfoDTO = employeService.toUserInfoDTO(SecurityUtils.getConnectedUser().getId());
		 userInfoDTO.setNavigation(Navigation.buildNavigationDefault(userInfoDTO,contextPath));
		 return userInfoDTO;
	} catch (Exception e) {
		LOG.error(e.getMessage(), e);
		throw e;
	}
}


}