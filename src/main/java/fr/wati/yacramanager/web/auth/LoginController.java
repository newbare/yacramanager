package fr.wati.yacramanager.web.auth;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	@RequestMapping(value = "/auth/login/**")
	public ModelAndView login(
			@RequestParam(value = "error", defaultValue = "false", required = false) boolean error,
			HttpSession httpSession) {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("auth/login");
		if (error) {
			modelAndView.addObject("error", true);
			String exeptionMessage = null;
			if (httpSession != null) {
				if (httpSession.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") instanceof Exception) {
					exeptionMessage = ((Exception) httpSession
							.getAttribute("SPRING_SECURITY_LAST_EXCEPTION"))
							.getMessage();
					modelAndView.addObject("errorMessage", exeptionMessage);
				}

			}
		}
		return modelAndView;
	}
}
