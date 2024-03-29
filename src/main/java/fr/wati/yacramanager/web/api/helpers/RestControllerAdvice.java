/**
 * 
 */
package fr.wati.yacramanager.web.api.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.web.api.AbsenceController;
import fr.wati.yacramanager.web.api.ActivityReportController;
import fr.wati.yacramanager.web.api.ClientController;
import fr.wati.yacramanager.web.api.CompanyController;
import fr.wati.yacramanager.web.api.CustomMapEditor;
import fr.wati.yacramanager.web.api.EmployesProjectsController;
import fr.wati.yacramanager.web.api.GlobalSearchController;
import fr.wati.yacramanager.web.api.InvoiceController;
import fr.wati.yacramanager.web.api.NoteDeFraisController;
import fr.wati.yacramanager.web.api.ProjectController;
import fr.wati.yacramanager.web.api.RestServiceException;
import fr.wati.yacramanager.web.api.TaskRestController;
import fr.wati.yacramanager.web.api.UserRestController;
import fr.wati.yacramanager.web.api.WorkLogRestController;
import fr.wati.yacramanager.web.auth.AuthenticationController;
import fr.wati.yacramanager.web.dto.ExceptionDto;

/**
 * @author Rachid Ouattara
 * 
 */
@ControllerAdvice(assignableTypes = { NoteDeFraisController.class,
		AbsenceController.class, CompanyController.class,
		ClientController.class, UserRestController.class,
		ProjectController.class, WorkLogRestController.class,
		EmployesProjectsController.class, TaskRestController.class,
		ActivityReportController.class, AuthenticationController.class,
		InvoiceController.class,GlobalSearchController.class })
public class RestControllerAdvice {

	private Logger logger=LoggerFactory.getLogger(RestControllerAdvice.class);
	@Inject
	private SimpMessagingTemplate messagingTemplate;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		/*
		 * Handle case where need to map {"":""} as a map
		 */
		binder.registerCustomEditor(Map.class, new CustomMapEditor(
				HashMap.class, true));
	}

	@ExceptionHandler({ RestServiceException.class, ServiceException.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@SendToUser(value = "/queue/errors")
	public @ResponseBody ResponseEntity<ExceptionDto> handleRestException(Exception ex, HttpServletResponse response)
			throws IOException {
		// messagingTemplate.convertAndSendToUser(SecurityUtils.getConnectedUser().getUsername(),
		// "/queue/errors", ex.getMessage());
		logger.error(ex.getMessage(), ex);
		return new ResponseEntity<ExceptionDto>(new ExceptionDto(ExceptionUtils.getRootCauseMessage(ex)),HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
