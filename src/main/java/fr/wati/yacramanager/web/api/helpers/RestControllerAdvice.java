/**
 * 
 */
package fr.wati.yacramanager.web.api.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.wati.yacramanager.web.api.AbsenceController;
import fr.wati.yacramanager.web.api.ClientController;
import fr.wati.yacramanager.web.api.CompanyController;
import fr.wati.yacramanager.web.api.CustomMapEditor;
import fr.wati.yacramanager.web.api.NoteDeFraisController;
import fr.wati.yacramanager.web.api.UserRestController;

/**
 * @author Rachid Ouattara
 * 
 */
@ControllerAdvice(assignableTypes = { NoteDeFraisController.class,
		AbsenceController.class, CompanyController.class,
		ClientController.class,UserRestController.class })
public class RestControllerAdvice {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		/*
		 * Handle case where need to map {"":""} as a map
		 */
		binder.registerCustomEditor(Map.class, new CustomMapEditor(
				HashMap.class, true));
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String handleRestException(Exception ex, HttpServletResponse response) throws IOException
	{
		return ex.getMessage();

	}
}
