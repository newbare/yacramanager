/**
 * 
 */
package fr.wati.yacramanager.web.app;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.utils.SecurityUtils;

/**
 * @author Rachid Ouattara
 * 
 */
@Controller
@RequestMapping(value = "/app/report")
public class ReportController {

	
	@RequestMapping(value = "/activityReport")
	public ModelAndView index(Principal principal, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("activityReportPDF");
		request. setAttribute("outputType", "pdf");
		request.setAttribute("filename", "activityReport_"+principal.getName()+".pdf");
		Employe connectedUser = SecurityUtils.getConnectedUser();
		modelAndView.addObject("user", connectedUser);
		modelAndView.addObject("format", "pdf");
		return modelAndView;
	}

}
