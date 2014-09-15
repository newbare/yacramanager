/**
 * 
 */
package fr.wati.yacramanager.web.api.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.wati.yacramanager.web.api.AbsenceController;
import fr.wati.yacramanager.web.api.AuthenticationController;
import fr.wati.yacramanager.web.api.ClientController;
import fr.wati.yacramanager.web.api.CompanyController;
import fr.wati.yacramanager.web.api.CraController;
import fr.wati.yacramanager.web.api.CustomMapEditor;
import fr.wati.yacramanager.web.api.NoteDeFraisController;
import fr.wati.yacramanager.web.api.ProjectController;
import fr.wati.yacramanager.web.api.RestServiceException;
import fr.wati.yacramanager.web.api.TaskRestController;
import fr.wati.yacramanager.web.api.UserRestController;
import fr.wati.yacramanager.web.api.WorkLogRestController;

/**
 * @author Rachid Ouattara
 * 
 */
@ControllerAdvice(assignableTypes = { NoteDeFraisController.class,
		AbsenceController.class, CompanyController.class,
		ClientController.class, UserRestController.class,
		ProjectController.class, WorkLogRestController.class,
		TaskRestController.class,CraController.class,AuthenticationController.class})
public class RestControllerAdvice {

	private Logger logger=LoggerFactory.getLogger(RestControllerAdvice.class);
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		/*
		 * Handle case where need to map {"":""} as a map
		 */
		binder.registerCustomEditor(Map.class, new CustomMapEditor(
				HashMap.class, true));
	}

	@ExceptionHandler({ RestServiceException.class, Exception.class })
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@SendToUser(value = "/queue/errors")
	@ResponseBody
	public String handleRestException(Exception ex, HttpServletResponse response)
			throws IOException {
		// messagingTemplate.convertAndSendToUser(SecurityUtils.getConnectedUser().getUsername(),
		// "/queue/errors", ex.getMessage());
		logger.error(ex.getMessage(), ex);
		return ex.getMessage();

	}
}
