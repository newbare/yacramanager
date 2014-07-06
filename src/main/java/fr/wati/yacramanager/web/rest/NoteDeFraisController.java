/**
 * 
 */
package fr.wati.yacramanager.web.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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

import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.services.NoteDeFraisService;
import fr.wati.yacramanager.utils.DtoMapper;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsenceDTO;
import fr.wati.yacramanager.web.dto.NoteDeFraisDTO;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

/**
 * @author Rachid Ouattara
 *
 */
@RestController()
@RequestMapping("/rest/frais")
public class NoteDeFraisController implements RestCrudController<NoteDeFraisDTO> {

	@Autowired
	private NoteDeFraisService noteDeFraisService;

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody NoteDeFraisDTO read(@PathVariable("id") Long id) {
		return DtoMapper.map(noteDeFraisService.findOne(id));
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable("id") Long id, @RequestBody NoteDeFraisDTO dto) {
		NoteDeFrais findOne = noteDeFraisService.findOne(id);
		noteDeFraisService.save(dto.toNoteDeFrais(findOne));
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public ResponseWrapper<List<NoteDeFraisDTO>> getAll(@RequestParam(required=false) Integer page,@RequestParam(required=false) Integer size,@RequestParam(required=false,defaultValue="date") String orderBy) {
		if(page==null){
			page=0;
		}
		if(size==null){
			size=100;
		}
		PageRequest pageable=new PageRequest(page, size,new Sort(new Order(Direction.DESC,orderBy)));
		Page<NoteDeFrais> findByPersonne = noteDeFraisService.findByPersonne(SecurityUtils.getConnectedUser(), pageable);
		return new ResponseWrapper<List<NoteDeFraisDTO>>(DtoMapper.mapNoteDeFrais(findByPersonne),findByPersonne.getTotalElements());
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> create(@RequestBody NoteDeFraisDTO dto) {
		NoteDeFrais noteDeFrais = dto.toNoteDeFrais();
		noteDeFrais.setDate(new Date());
		noteDeFrais.setPersonne(SecurityUtils.getConnectedUser());
		noteDeFraisService.save(noteDeFrais);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Override
	@RequestMapping(value = "/{id}",method=RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		noteDeFraisService.delete(id);
	}

	@RequestMapping(value = "/types", method = RequestMethod.GET)
	public @ResponseBody List<TypeAbsenceDTO> getTypeAbsences() {
		List<TypeAbsenceDTO> absenceDTOs=new ArrayList<>();
		for(TypeAbsence absence:TypeAbsence.values()){
			absenceDTOs.add(TypeAbsenceDTO.fromEnum(absence));
		}
		return absenceDTOs;
	}
}
