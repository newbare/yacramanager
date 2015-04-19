package fr.wati.yacramanager.web.api;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.services.ActivityReportService;
import fr.wati.yacramanager.services.CraService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.web.dto.CraDTO;
import fr.wati.yacramanager.web.dto.CraDetailsDTO;

@RestController
@RequestMapping("/app/api/activity-report")
public class ActivityReportController {

	private Logger logger = LoggerFactory
			.getLogger(ActivityReportController.class);
	@Inject
	private CraService craService;

	@Inject
	private EmployeService employeService;

	@Inject
	private ActivityReportService activityReportService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// binder.registerCustomEditor(Date.class,
		// new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
	}

	@RequestMapping(method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	CraDTO getCra(
			@RequestParam(value = "start", required = true) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
			@RequestParam(value = "end", required = true) @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
		CraDTO craDTO = craService.generateCra(
				SecurityUtils.getConnectedUser(), startDate, endDate);
		return craDTO;
	}

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	CraDetailsDTO getCraDetails(
			@RequestParam(value = "employeIds", required = true) List<Long> employeIds,
			@RequestParam(value = "startDate", required = true) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
			@RequestParam(value = "endDate", required = true) @DateTimeFormat(iso = ISO.DATE) LocalDate endDate)
			throws RestServiceException {
		CraDetailsDTO craDetailsDTO;
		try {
			craDetailsDTO = craService.generateCraDetail(
					employeService.findAll(employeIds), startDate, endDate);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new RestServiceException(e);
		}
		return craDetailsDTO;
	}

	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@Timed
	public void submitNewActivityReport(
			@RequestParam(value = "employeId") Long employeId,
			@RequestParam(value = "startDate") @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(iso = ISO.DATE) LocalDate endDate)
			throws RestServiceException {
		try {
			Employe employe = employeService.findOne(employeId);
			activityReportService.submitNewActivityReport(employe, startDate,
					endDate);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new RestServiceException(e);
		}
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@Timed
	public void cancelSubmittedActivityReport(
			@RequestParam(value = "employeId") Long employeId,
			@RequestParam(value = "startDate") @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(iso = ISO.DATE) LocalDate endDate)
			throws RestServiceException {
		try {
			Employe employe = employeService.findOne(employeId);
			activityReportService.cancelSubmittedActivityReport(employe,startDate,endDate);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new RestServiceException(e);
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@Timed
	public void approveSubmittedActivityReport(
			@RequestParam(value = "employeId") Long employeId,
			@RequestParam(value = "startDate") @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(iso = ISO.DATE) LocalDate endDate)
			throws RestServiceException {
		try {
			Employe employe = employeService.findOne(employeId);
			activityReportService.approveSubmittedActivityReport(employe, activityReportService.findByEmployeAndStartDateAndEndDate(employe, startDate, endDate));
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new RestServiceException(e);
		}
	}
	
}
