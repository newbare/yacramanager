/**
 * 
 */
package fr.wati.yacramanager.web.app;

import java.security.Principal;

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
	
	@RequestMapping(value = "/email")
	public ModelAndView index(Principal principal){
		ModelAndView modelAndView=new ModelAndView("activationEmailPDF");
		Employe connectedUser = SecurityUtils.getConnectedUser();
		modelAndView.addObject("user", connectedUser);
		return modelAndView;
	}

}
