package fr.wati.yacramanager.web.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import fr.wati.yacramanager.beans.Personne;
import fr.wati.yacramanager.dao.PersonRepository;
import fr.wati.yacramanager.dao.PersonneDto;
import fr.wati.yacramanager.services.PersonService;
import fr.wati.yacramanager.utils.DtoMapper;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.web.dto.Navigation;
import fr.wati.yacramanager.web.dto.ResponseWrapper;
import fr.wati.yacramanager.web.dto.UserInfoDTO;

@Controller
@RequestMapping(value = "/app/api/users")
public class UserRestController implements RestCrudController<PersonneDto>{

	private static final Log LOG=LogFactory.getLog(UserRestController.class);
	@Autowired
	private PersonService personneService;
	@Autowired
	private PersonRepository personRepository;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public @ResponseBody PersonneDto read(@PathVariable("id") Long id) {
	  
	Personne findOne = personRepository.findOne(id);
	if(findOne!=null){
		return DtoMapper.map(findOne);
	}
    return null;
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(@PathVariable("id") Long id, @RequestBody PersonneDto personneDto) {
	  Personne personne = personRepository.findOne(id.longValue());
	  personne.setPrenom(personneDto.getPrenom());
	  personne.setNom(personneDto.getNom());
	  personne.setUsername(personneDto.getUsername());
	  personne.setCivilite(personneDto.getCivilite());
	  personne.setDateNaissance(personneDto.getDateNaissance());
	  personne.getContact().setEmail(personneDto.getEmail());
	  personneService.save(personne);
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<String> create(@RequestBody PersonneDto personneDto) {
	  Personne personne=new Personne();
	  personne.setPrenom(personneDto.getPrenom());
	  personne.setNom(personneDto.getNom());
	  personne.setUsername(personneDto.getUsername());
	  personne.setPassword(personneDto.getPassword());
	  personne.setCivilite(personneDto.getCivilite());
	  personne.setDateNaissance(personneDto.getDateNaissance());
	  personne.getContact().setEmail(personneDto.getEmail());
	  personneService.save(personne);
	  return new ResponseEntity<String>(personneDto.getNom()+" created", HttpStatus.CREATED);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id) {
	  personneService.delete(id.longValue());
  }

@Override
@RequestMapping(method=RequestMethod.GET)
public @ResponseBody ResponseWrapper<List<PersonneDto>> getAll(@RequestParam(required=false) Integer page,@RequestParam(required=false) Integer size,@RequestParam(required=false,defaultValue="id") String orderBy) {
	if(page==null){
		page=0;
	}
	if(size==null){
		size=100;
	}
	Pageable pageRequest = new PageRequest(page, size,new Sort(new Order(orderBy)));
	Page<Personne> all = personRepository.findAll(pageRequest);
	return new ResponseWrapper<List<PersonneDto>>(DtoMapper.mapPersonne(all),all.getTotalElements());
}

@RequestMapping(value="/user-info", method=RequestMethod.GET)
public @ResponseBody UserInfoDTO getConnectedUserInfo(HttpServletRequest request) throws Exception{
	try {
		String contextPath=request.getContextPath();
		 UserInfoDTO userInfoDTO = personneService.toUserInfoDTO(SecurityUtils.getConnectedUser().getId());
		 userInfoDTO.setNavigation(Navigation.buildNavigationDefault(userInfoDTO,contextPath));
		 return userInfoDTO;
	} catch (Exception e) {
		LOG.error(e.getMessage(), e);
		throw e;
	}
}


}