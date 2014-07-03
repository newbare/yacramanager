package fr.wati.yacramanager.web.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.services.AbsenceService;
import fr.wati.yacramanager.utils.DtoMapper;
import fr.wati.yacramanager.web.dto.AbsenceDTO;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsenceDTO;

@RestController()
@RequestMapping("/rest/absences")
public class AbsenceController implements RestCrudController<AbsenceDTO, Long> {

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
	}

	@Override
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<AbsenceDTO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> create(AbsenceDTO dto) {
		absenceService.save(new Absence());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Override
	public void delete(long id) {
		absenceService.delete(id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/types", method = RequestMethod.GET)
	public @ResponseBody List<TypeAbsenceDTO> getTypeAbsences() {
		List<TypeAbsenceDTO> absenceDTOs=new ArrayList<>();
		for(TypeAbsence absence:TypeAbsence.values()){
			absenceDTOs.add(TypeAbsenceDTO.fromEnum(absence));
		}
		return absenceDTOs;
	}
}
