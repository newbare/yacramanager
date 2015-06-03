/**
 * 
 */
package fr.wati.yacramanager.web;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import fr.wati.yacramanager.utils.SecurityUtils;

/**
 * @author Rachid Ouattara
 *
 */
@Controller
public class DefaultController {

	@Inject
	private Environment env;
	@RequestMapping(value = "/")
	public ModelAndView index(HttpSession httpSession){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		modelAndView.addObject("userName", SecurityUtils.getConnectedUser()!=null?SecurityUtils.getConnectedUser().getFullName():null);
		return modelAndView;
	}
	
}
