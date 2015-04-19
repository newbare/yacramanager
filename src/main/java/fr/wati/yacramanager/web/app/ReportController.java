/**
 * 
 */
package fr.wati.yacramanager.web.app;

import java.security.Principal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.services.CraService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ServiceException;

/**
 * @author Rachid Ouattara
 * 
 */
@Controller
@RequestMapping(value = "/app/report")
public class ReportController {

	@Inject
	private EmployeService employeService;
	
	@Inject
	private CraService craService;
	
	@RequestMapping(value = "/activityReport/{employeId}")
	public ModelAndView index(Principal principal, HttpServletRequest request,
			HttpServletResponse response,@PathVariable("employeId") Long employeId,
			@RequestParam(value = "start", required = true) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
			@RequestParam(value = "end", required = true) @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) throws ServiceException {
		ModelAndView modelAndView = new ModelAndView("activityReportPDF");
		request. setAttribute("outputType", "pdf");
		Employe employe = employeService.findOne(employeId);
		//request.setAttribute("filename", "activityReport_"+employe.getFirstName()+"_"+employe.getLastName()+".pdf");
		modelAndView.addObject("employe", employe);
		modelAndView.addObject("start", startDate);
		modelAndView.addObject("end", endDate);
		modelAndView.addObject("craDetailsDTO", craService.generateCraDetail(Lists.newArrayList(employe), startDate, endDate));
		modelAndView.addObject("format", "pdf");
		return modelAndView;
	}

}
