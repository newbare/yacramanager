package fr.wati.yacramanager.web.auth;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.CompanyTempInvitation;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.config.social.DefaultConnectionSignUp;
import fr.wati.yacramanager.dao.JdbcCompanyInvitationRepository;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.services.UserService;
import fr.wati.yacramanager.web.ResourceNotFoundException;
import fr.wati.yacramanager.web.api.RestServiceException;
import fr.wati.yacramanager.web.dto.RegistrationDTO;

@Controller
public class AuthController {

	private final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Inject 
	private Environment env;
	@Inject
	private UserService userService;
	
	@Inject
	private EmployeService employeService;

	@Inject
	private DefaultConnectionSignUp connectionSignUp;

	@Inject
	private JdbcCompanyInvitationRepository companyInvitationRepository;

	@RequestMapping(value = "/auth/login")
	public ModelAndView login(
			@RequestParam(value = "error", defaultValue = "false", required = false) boolean error,
			HttpSession httpSession, Model model) {

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

		if (model != null) {
			log.debug("Redirect to login after activation");
			if (model.containsAttribute("activationFailed")) {
				modelAndView.addObject("activationFailed",
						model.asMap().get("activationFailed"));
			}
			if (model.containsAttribute("activationMessage")) {
				modelAndView.addObject("activationMessage",
						model.asMap().get("activationMessage"));
			}
			if (model.containsAttribute("activationSuccess")) {
				modelAndView.addObject("activationSuccess",
						model.asMap().get("activationSuccess"));
			}
		}
		return modelAndView;
	}

	@RequestMapping(value = "/auth/activate", method = RequestMethod.GET)
	@Timed
	public RedirectView activateAccount(
			@RequestParam(value = "key") String key,
			final RedirectAttributes redirectAttrs) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/auth/login");
		Users user = userService.activateRegistration(key);
		if (user == null) {
			redirectAttrs.addFlashAttribute("activationFailed", true);
			redirectAttrs.addFlashAttribute("activationMessage",
					"No account found for given activation key");
		} else {
			redirectAttrs.addFlashAttribute("activationSuccess", true);
			redirectAttrs.addFlashAttribute("activationMessage",
					"The account has been activated !");
		}
		return redirectView;
	}

	@RequestMapping(value = "/auth/**")
	public RedirectView redirectToLogin() {
		RedirectView redirectView = new RedirectView("login", false);
		return redirectView;
	}

	@RequestMapping(value = "/auth/register")
	public ModelAndView register(
			@RequestParam(value = "oauth_user", defaultValue = "false", required = false) boolean oauthUser,
			HttpSession httpSession, WebRequest webRequest,
			HttpServletRequest request, Model model) {

		ModelAndView modelAndView = new ModelAndView();
		if (oauthUser
				&& SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			Connection<?> connection = ProviderSignInUtils
					.getConnection(webRequest);
			RegistrationDTO registrationDTO = connectionSignUp.fromConnection(
					connection).fromConnection(connection);
			modelAndView.addObject("preFillRegistrationDTO", registrationDTO);
		}
		if (model != null && model.containsAttribute("invitation")) {
			CompanyTempInvitation invitation = (CompanyTempInvitation) model
					.asMap().get("invitation");
			log.debug("Registration after invitation" + invitation);
			modelAndView.addObject("invitation", invitation);
		}
		modelAndView.addObject("oauthUser", oauthUser);
		modelAndView.setViewName("auth/register");
		return modelAndView;
	}

	@RequestMapping(value = "/auth/invitation/accept", method = RequestMethod.GET)
	public RedirectView acceptInvitation(
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "user", required = true) String user,
			@RequestParam(value = "company", required = true) String company,
			final RedirectAttributes redirectAttrs) throws RestServiceException {
		CompanyTempInvitation invitation = companyInvitationRepository
				.findInvitationWithToken(user, company, token);
		RedirectView redirectView = new RedirectView();
		if (invitation != null) {
			Employe existingEmploye=employeService.findByEmail(invitation.getUserId());
			if(existingEmploye!=null){
				try {
					employeService.processInvitation(existingEmploye, invitation);
					redirectView.setUrl("/");
				} catch (ServiceException e) {
					log.error(e.getMessage(), e);
					throw new RestServiceException(e);
				}
			}else {
				redirectView.setUrl("/auth/register");
				redirectAttrs.addFlashAttribute("invitation", invitation);
			}
			return redirectView;
		}
		// Redirect to not found
		throw new ResourceNotFoundException("Invalid acceptation request");

	}

	@RequestMapping(value = "/auth/forgot-password")
	public ModelAndView recoverPassword(HttpSession httpSession) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("auth/forgot-password");
		return modelAndView;
	}
}
