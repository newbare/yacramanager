package fr.wati.yacramanager.web.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.wati.yacramanager.services.AbsenceService;
import fr.wati.yacramanager.web.dto.AbsenceDTO;

@RestController()
@RequestMapping("/rest/absences")
public class AbsenceController implements RestCrudController<AbsenceDTO, Long> {

	private AbsenceService absenceService;

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public AbsenceDTO read(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(Long id, AbsenceDTO dto) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long ID) {
		// TODO Auto-generated method stub

	}

}
