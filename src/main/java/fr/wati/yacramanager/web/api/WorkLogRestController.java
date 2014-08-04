package fr.wati.yacramanager.web.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import fr.wati.yacramanager.beans.WorkLog;
import fr.wati.yacramanager.services.WorkLogService;
import fr.wati.yacramanager.utils.DtoMapper;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.web.dto.ResponseWrapper;
import fr.wati.yacramanager.web.dto.WorkLogDTO;

@Controller
@RequestMapping(value = "/app/api/worklog")
public class WorkLogRestController implements RestCrudController<WorkLogDTO>{

	@Autowired
	private WorkLogService workLogService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<List<WorkLogDTO>> getEvents(
			@RequestParam(value = "start", required = true) long start,
			@RequestParam(value = "end", required = true) long end) {
		// We receive time in second
		Date startDate = new Date(start * 1000);
		Date endDate = new Date(end * 1000);
		List<WorkLog> workLogs = workLogService.findByEmployeAndStartDateBetween(SecurityUtils.getConnectedUser(), startDate, endDate);
		if(workLogs!=null && !workLogs.isEmpty()){
			List<WorkLogDTO> dtos = DtoMapper.mapWorkLogs(workLogs);
			return new ResponseEntity<List<WorkLogDTO>>(dtos, HttpStatus.OK);
		}
		return new ResponseEntity<List<WorkLogDTO>>(HttpStatus.OK);
	}


	@RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
	public ResponseEntity<WorkLogDTO> getEventDetails(@PathVariable("id") Long id){
		WorkLog workLog = workLogService.findOne(id);
		if(workLog!=null){
			WorkLogDTO workLogDTO=DtoMapper.mapForDetails(workLog);
			return new ResponseEntity<WorkLogDTO>(workLogDTO, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	WorkLogDTO read(@PathVariable("id") Long id) {
		return DtoMapper.map(workLogService.findOne(id));
	}


	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable("id") Long id, @RequestBody WorkLogDTO dto) {
		WorkLog findOne = workLogService.findOne(id);
		workLogService.save(dto.toWorkLog(findOne));
	}


	@Override
	public ResponseWrapper<List<WorkLogDTO>> getAll(Integer page,
			Integer Integer, Map<String, String> sort, String filter)
			throws RestServiceException {
		throw new RestServiceException("operation not allowed");
	}


	@Override
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> create(@RequestBody WorkLogDTO dto) {
		WorkLog workLog = dto.toWorkLog();
		workLog.setEmploye(SecurityUtils.getConnectedUser());
		workLogService.save(workLog);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}


	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		workLogService.delete(id);
	}
	

	
}
