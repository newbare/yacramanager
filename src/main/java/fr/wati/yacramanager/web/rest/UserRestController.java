package fr.wati.yacramanager.web.rest;

import java.util.List;

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
import fr.wati.yacramanager.utils.JqgridFilter;
import fr.wati.yacramanager.utils.JqgridObjectMapper;
import fr.wati.yacramanager.web.dto.JqgridResponse;

@Controller
@RequestMapping(value = "/rest/users")
public class UserRestController implements RestCrudController<PersonneDto, Long>{

	@Autowired
	private PersonService personneService;
	@Autowired
	private PersonRepository personRepository;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public @ResponseBody PersonneDto read(@PathVariable("id") Long id) {
	  
	Personne findOne = personRepository.findOne(Long.valueOf(String.valueOf(id)));
	if(findOne!=null){
		return DtoMapper.map(findOne);
	}
    return null;
  }

  @RequestMapping(value="/jqgrid",method = RequestMethod.GET)
  public
  @ResponseBody
  JqgridResponse<PersonneDto> records(@RequestParam(value="_search",required=false) Boolean search,
			@RequestParam(value = "filters", required = false) String filters,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows,
			@RequestParam(value = "sidx", required = false) String sidx,
			@RequestParam(value = "sord", required = false) String sord) {

		Pageable pageRequest = new PageRequest(page - 1, rows);

		if(search==null) search=false;
		if (search == true) {
			return getFilteredRecords(filters, pageRequest);

		}

		Page<Personne> personnes = personRepository.findAll(pageRequest);
		List<PersonneDto> dtos=DtoMapper.mapPersonne(personnes);
		JqgridResponse<PersonneDto> response = new JqgridResponse<PersonneDto>();
		response.setRows(dtos);
		response.setRecords(Long.valueOf(personnes.getTotalElements()).toString());
		response.setTotal(Integer.valueOf(personnes.getTotalPages()).toString());
		response.setPage(Integer.valueOf(personnes.getNumber() + 1).toString());

		return response;
	}

  /**
	 * Helper method for returning filtered records
	 */
	public JqgridResponse<PersonneDto> getFilteredRecords(String filters,
			Pageable pageRequest) {
		String qnom = null;
		String qprenom = null;

		JqgridFilter jqgridFilter = JqgridObjectMapper.map(filters);
		for (JqgridFilter.Rule rule : jqgridFilter.getRules()) {
			if (rule.getField().equals("nom"))
				qnom = rule.getData();
			else if (rule.getField().equals("prenom"))
				qprenom = rule.getData();
		}

		Page<Personne> personnes = null;
		if (qnom != null)
			personnes = personRepository.findByNomLike("%" + qnom + "%",
					pageRequest);
		if (qprenom != null)
			personnes = personRepository.findByPrenomLike("%" + qprenom + "%",
					pageRequest);

		List<PersonneDto> matieresDtos = DtoMapper.mapPersonne(personnes);
		JqgridResponse<PersonneDto> response = new JqgridResponse<PersonneDto>();
		response.setRows(matieresDtos);
		response.setRecords(Long.valueOf(personnes.getTotalElements()).toString());
		response.setTotal(Integer.valueOf(personnes.getTotalPages()).toString());
		response.setPage(Integer.valueOf(personnes.getNumber() + 1).toString());
		return response;
	}
  
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(@PathVariable("id") Long id, @RequestBody PersonneDto personneDto) {
	  Personne personne = personRepository.findOne(id);
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
  public void delete(@PathVariable("id") long id) {
	  personneService.delete(id);
  }

@Override
@RequestMapping(method=RequestMethod.GET)
public @ResponseBody List<PersonneDto> getAll(@RequestParam(required=false) Integer page,@RequestParam(required=false) Integer size,@RequestParam(required=false,defaultValue="id") String orderBy) {
	if(page==null){
		page=0;
	}
	if(size==null){
		size=100;
	}
	Pageable pageRequest = new PageRequest(page, size,new Sort(new Order(orderBy)));
	return DtoMapper.mapPersonne(personRepository.findAll(pageRequest));
}



}