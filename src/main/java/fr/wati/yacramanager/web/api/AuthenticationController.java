/**
 * 
 */
package fr.wati.yacramanager.web.api;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.MailService;
import fr.wati.yacramanager.services.UserService;
import fr.wati.yacramanager.web.dto.RegistrationDTO;

/**
 * @author Rachid Ouattara
 * 
 */
@RestController
@RequestMapping("/auth/api")
public class AuthenticationController {

	@Autowired
	private EmployeService employeService;

	@Autowired
	private UserService userService;

	@Autowired
	private MailService mailService;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> register(
			@RequestBody RegistrationDTO registrationDTO,
			HttpServletRequest request, HttpServletResponse response,
			Locale locale) {
		Employe registerEmploye = employeService
				.registerEmploye(registrationDTO);
		String content = createHtmlContentFromTemplate(registerEmploye, locale,
				request, response);
		mailService.sendActivationEmail(
				registerEmploye.getContact().getEmail(), content, locale);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/activate", method = RequestMethod.GET)
	@Timed
	public ModelAndView activateAccount(
			@RequestParam(value = "key") String key) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("auth/login");
		
		Users user = userService.activateRegistration(key);
		if (user == null) {
			modelAndView.addObject("activationFailed", true);
			modelAndView.addObject("activationMessage", "No account found for given activation key");
		}else {
			modelAndView.addObject("activationSuccess", true);
			modelAndView.addObject("activationMessage", "The account has been activated !");
		}
		return modelAndView;
	}

	@RequestMapping(value = "/password-recovery", method = RequestMethod.POST,produces={MediaType.APPLICATION_JSON_VALUE})
	@Timed
	public ResponseEntity<String> recoverPassword(@RequestBody String email,HttpServletRequest request, HttpServletResponse response,
			Locale locale) {
		Employe employe = employeService.findByContact_Email(email);
		if(employe!=null){
			String resetPassword = employeService.resetPassword(employe);
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("user", employe);
			variables.put("resetPassword", resetPassword);
			IWebContext context = new SpringWebContext(request, response,
					request.getServletContext(), locale, variables,
					WebApplicationContextUtils.getWebApplicationContext(request
							.getServletContext()));
			String content =templateEngine.process("recoveryPasswordEmail", context);
			mailService.sendEmail(employe.getContact().getEmail(), "Password recovery", content, false, true);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private String createHtmlContentFromTemplate(final Users user,
			final Locale locale, final HttpServletRequest request,
			final HttpServletResponse response) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("user", user);
		variables.put("baseUrl", request.getScheme() + "://" + // "http" + "://
				request.getServerName() + // "myhost"
				":" + request.getServerPort()+request.getContextPath());
		IWebContext context = new SpringWebContext(request, response,
				request.getServletContext(), locale, variables,
				WebApplicationContextUtils.getWebApplicationContext(request
						.getServletContext()));
		return templateEngine.process("activationEmail", context);
	}
}
