package fr.wati.yacramanager.web.api;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.services.CraService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.web.dto.CraDTO;
import fr.wati.yacramanager.web.dto.CraDetailsDTO;

@Controller
@RequestMapping("/app/api/cra")
public class CraController {

	@Autowired
	private CraService craService;
	
	@Autowired
	private EmployeService employeService;
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
//        binder.registerCustomEditor(Date.class,
//                  new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
    }
	
	@RequestMapping(method=RequestMethod.GET)
	@Timed
	public @ResponseBody CraDTO getCra(@RequestParam(value="start", required=true) @DateTimeFormat(iso=ISO.DATE_TIME) DateTime startDate, @RequestParam(value="end", required=true) @DateTimeFormat(iso=ISO.DATE_TIME) DateTime endDate){
		CraDTO craDTO=craService.generateCra(SecurityUtils.getConnectedUser(), startDate, endDate);
		return craDTO;
	}
	
	@RequestMapping(value="/details", method=RequestMethod.GET)
	@Timed
	public @ResponseBody CraDetailsDTO getCraDetails(@RequestParam(value="employeIds", required=true) List<Long> employeIds, @RequestParam(value="start", required=true) @DateTimeFormat(iso=ISO.DATE_TIME) DateTime startDate, @RequestParam(value="end", required=true) @DateTimeFormat(iso=ISO.DATE_TIME) DateTime endDate){
		CraDetailsDTO craDetailsDTO=craService.generateCraDetail(employeService.findAll(employeIds), startDate, endDate);
		return craDetailsDTO;
	}
}
