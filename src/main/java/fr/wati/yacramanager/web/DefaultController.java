package fr.wati.yacramanager.web;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import fr.wati.yacramanager.utils.SecurityUtils;

@Controller
public class DefaultController {

	@RequestMapping(value = "/")
	public ModelAndView index(Principal principal){
		ModelAndView modelAndView=new ModelAndView("index");
		modelAndView.addObject("userName", SecurityUtils.getConnectedUser().getFullName());
		return modelAndView;
	}
}
