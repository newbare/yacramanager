package fr.wati.yacramanager.web.api;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
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

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.beans.WorkLog;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.TaskService;
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
	@Autowired
	private EmployeService employeService;
	@Autowired
	private TaskService taskService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity<List<WorkLogDTO>> getEvents(
			@RequestParam(value = "start", required = true) long start,
			@RequestParam(value = "end", required = true) long end) {
		// We receive time in second
		DateTime startDate = new DateTime(start * 1000);
		DateTime endDate = new DateTime(end * 1000);
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
	public ResponseEntity<String> create(@RequestBody WorkLogDTO dto) throws RestServiceException{
		if(dto.getTaskId()==null){
			throw new RestServiceException("No given task ID error");
		}
		dto.setValidationStatus(ValidationStatus.WAIT_FOR_APPROVEMENT);
		WorkLog workLog = dto.toWorkLog();
		workLog.setCreatedDate(new DateTime());
		if(dto.getEmployeId()==null){
			workLog.setEmploye(SecurityUtils.getConnectedUser());
		}else {
			Employe employe=employeService.findOne(dto.getEmployeId());
			workLog.setEmploye(employe);
		}
		Task task=taskService.findOne(dto.getTaskId());
		workLog.setTask(task);
		workLogService.save(workLog);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}


	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		workLogService.delete(id);
	}
	

	
}
