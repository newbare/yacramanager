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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.services.AbsenceService;
import fr.wati.yacramanager.utils.DtoMapper;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.web.dto.AbsenceDTO;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsenceDTO;

@RestController()
@RequestMapping("/rest/absences")
public class AbsenceController implements RestCrudController<AbsenceDTO, Long> {

	@Autowired
	private AbsenceService absenceService;

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody AbsenceDTO read(Long id) {
		return DtoMapper.map(absenceService.findOne(id));
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(Long id, AbsenceDTO dto) {
		Absence findOne = absenceService.findOne(id);
		absenceService.save(dto.toAbsence(findOne));
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<AbsenceDTO> getAll(@RequestParam(required=false) Integer page,@RequestParam(required=false) Integer size,@RequestParam(required=false,defaultValue="date") String orderBy) {
		if(page==null){
			page=0;
		}
		if(size==null){
			size=100;
		}
		PageRequest pageable=new PageRequest(page, size,new Sort(new Order(Direction.DESC,orderBy)));
		Page<Absence> findByPersonne = absenceService.findByPersonne(SecurityUtils.getConnectedUser(), pageable);
		return DtoMapper.mapAbsences(findByPersonne);
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> create(@RequestBody AbsenceDTO dto) {
		Absence absence = dto.toAbsence();
		absence.setDate(new Date());
		absence.setPersonne(SecurityUtils.getConnectedUser());
		absenceService.save(absence);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Override
	public void delete(long id) {
		absenceService.delete(id);
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
