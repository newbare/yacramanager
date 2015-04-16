package fr.wati.yacramanager.web.auth;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import fr.wati.yacramanager.config.social.DefaultConnectionSignUp;
import fr.wati.yacramanager.web.dto.RegistrationDTO;

@Controller
public class AuthController {

	@Autowired
	private DefaultConnectionSignUp connectionSignUp;
	
	@RequestMapping(value = "/auth/login")
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

	@RequestMapping(value = "/auth/")
	public RedirectView redirectToLogin() {
		RedirectView redirectView = new RedirectView("login",false);
		return redirectView;
	}
	
	@RequestMapping(value = "/auth/register")
	public ModelAndView register(
			@RequestParam(value = "oauth_user", defaultValue = "false", required = false) boolean oauthUser,
			HttpSession httpSession,WebRequest request) {
		
		ModelAndView modelAndView = new ModelAndView();
		if(oauthUser && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
			Connection<?> connection = ProviderSignInUtils.getConnection(request);
			RegistrationDTO registrationDTO=connectionSignUp.fromConnection(connection).fromConnection(connection);
			modelAndView.addObject("preFillRegistrationDTO", registrationDTO);
		}
		
		modelAndView.addObject("oauthUser", oauthUser);
		modelAndView.setViewName("auth/register");
		return modelAndView;
	}

	@RequestMapping(value = "/auth/forgot-password")
	public ModelAndView recoverPassword(HttpSession httpSession) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("auth/forgot-password");
		return modelAndView;
	}
}
