package fr.wati.yacramanager.web.app;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApplicationController {

	@RequestMapping(value = "/app/view/**")
	public ModelAndView index(Principal principal) {
		ModelAndView modelAndView = new ModelAndView("app/index");
		// modelAndView.addObject("userFirstName",
		// connectedUser.getFirstName());
		// modelAndView.addObject("userId", connectedUser.getId());
		// modelAndView.addObject("userCompanyName",
		// connectedUser.getCompany().getName());
		// modelAndView.addObject("userCompanyId",
		// connectedUser.getCompany().getId());
		return modelAndView;
	}

}
