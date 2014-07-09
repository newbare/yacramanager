package fr.wati.yacramanager.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/error")
public class ErrorController {

	public ModelAndView error(HttpServletRequest httpServletRequest){
		ModelAndView modelAndView=new ModelAndView("error");
		modelAndView.addObject("status", httpServletRequest.getAttribute("javax.servlet.error.status_code"));
		modelAndView.addObject("reason", httpServletRequest.getAttribute("javax.servlet.error.message"));
		return modelAndView;
	}
}
