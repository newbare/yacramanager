package fr.wati.yacramanager.web.app;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApplicationController {

	@Inject
	private Environment env;
	
	@RequestMapping(value = "/app/view/**")
	public ModelAndView index(Principal principal) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("app/index");
		return modelAndView;
	}

}
