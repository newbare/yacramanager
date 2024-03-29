/**
 * 
 */
package fr.wati.yacramanager.web.auth;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.MailService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.services.security.GoogleReCaptchaService;
import fr.wati.yacramanager.services.security.GoogleReCaptchaService.GoogleReCaptchaResponse;
import fr.wati.yacramanager.web.api.RestServiceException;
import fr.wati.yacramanager.web.dto.RegistrationDTO;

/**
 * @author Rachid Ouattara
 * 
 */
@RestController
@RequestMapping("/auth/api")
public class AuthenticationController {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Inject
    private EmployeService employeService;

    @Inject
    private MailService mailService;

    @Inject
    private SignInAdapter signInAdapter;

    @Inject
    private GoogleReCaptchaService reCaptchaService;

    @Resource(name = "templateEngine")
    private SpringTemplateEngine templateEngine;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<String> register(@RequestBody RegistrationDTO registrationDTO, HttpServletRequest request, NativeWebRequest webRequest, HttpServletResponse response,
            Locale locale) throws ServiceException, RestServiceException {
        try {
            // Google ReCaptcha check first
            GoogleReCaptchaResponse captchaResponse = reCaptchaService.validateCaptcha(registrationDTO.getCaptchaToken());
            if (!captchaResponse.isSuccess()) {
                return new ResponseEntity<String>(captchaResponse.getErrorCodes().toString(), HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RestServiceException(e);
        }

        registrationDTO.setLocale(locale);
        Employe registerEmploye = employeService.registerEmploye(registrationDTO, registrationDTO.isSocialUser());
        if (!registrationDTO.isSocialUser() && registrationDTO.getCompanyInvitation() == null) {
            String content = createHtmlContentFromTemplate(registerEmploye, locale, request, response);
            mailService.sendActivationEmail(registerEmploye.getContact().getEmail(), content, locale);
        } else if (registrationDTO.isSocialUser()) {
            if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                ProviderSignInAttempt providerSignInAttempt = (ProviderSignInAttempt) webRequest.getAttribute(ProviderSignInAttempt.SESSION_ATTRIBUTE,
                        RequestAttributes.SCOPE_SESSION);
                if (providerSignInAttempt != null) {
                    new ProviderSignInUtils().doPostSignUp(registrationDTO.getSocialUserId(), webRequest);
                    signInAdapter.signIn(registerEmploye.getUserName(), providerSignInAttempt.getConnection(), webRequest);
                }

            }
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/password-recovery", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
    @Timed
    public ResponseEntity<String> recoverPassword(@RequestBody String email, HttpServletRequest request, HttpServletResponse response, Locale locale) {
        Employe employe = employeService.findByEmail(email);
        if (employe != null) {
            String resetPassword = employeService.resetPassword(employe);
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("user", employe);
            variables.put("resetPassword", resetPassword);
            IWebContext context = new SpringWebContext(request, response, request.getServletContext(), locale, variables,
                    WebApplicationContextUtils.getWebApplicationContext(request.getServletContext()));
            String content = templateEngine.process("recoveryPasswordEmail", context);
            mailService.sendEmail(employe.getContact().getEmail(), "Password recovery", content, false, true);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("No registered user found with this id", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String createHtmlContentFromTemplate(final Users user, final Locale locale, final HttpServletRequest request, final HttpServletResponse response) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("user", user);
        variables.put("baseUrl",
                request.getScheme() + "://" + // "http" + "://
                        request.getServerName() + // "myhost"
                        ":" + request.getServerPort() + request.getContextPath());
        IWebContext context = new SpringWebContext(request, response, request.getServletContext(), locale, variables,
                WebApplicationContextUtils.getWebApplicationContext(request.getServletContext()));
        return templateEngine.process("activationEmail", context);
    }
}
