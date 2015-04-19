package fr.wati.yacramanager.services.security;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.CompanyAccountInfo;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.dao.repository.CompanyAccountInfoRepository;
import fr.wati.yacramanager.services.security.CustomUserDetailsService.CustomUserDetails;

@Component
public class PreAuthenticationChecker implements UserDetailsChecker {

	private static Logger logger = LoggerFactory
			.getLogger(PreAuthenticationChecker.class);
	protected MessageSourceAccessor messages = SpringSecurityMessageSource
			.getAccessor();

	@Inject
	private CompanyAccountInfoRepository companyAccountInfoRepository;

	public PreAuthenticationChecker() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public void check(UserDetails user) {
		if (!user.isAccountNonLocked()) {
			logger.debug("User account is locked");

			throw new LockedException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.locked",
					"User account is locked"), user);
		}

		if (!user.isEnabled()) {
			logger.debug("User account is disabled");

			throw new DisabledException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.disabled",
					"User is disabled"), user);
		}

		if (!user.isAccountNonExpired()) {
			logger.debug("User account is expired");

			throw new AccountExpiredException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.expired",
					"User account has expired"), user);
		}
		Company userCompany = ((Employe) ((CustomUserDetails) user)
				.getDomainUser()).getCompany();
		CompanyAccountInfo companyAccountInfo = companyAccountInfoRepository
				.findByCompany(userCompany);
		if(companyAccountInfo==null){
			throw new CompanyAuthenticationException("No account found for your company !");
		}
		if(companyAccountInfo.isLocked()){
			throw new CompanyAuthenticationException("Your company account is locked !");
		}
		if(companyAccountInfo.getExpiredDate().isBefore(new LocalDate())){
			throw new CompanyAuthenticationException("Your company account is expired !");
		}

	}

}
