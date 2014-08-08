/**
 * 
 */
package fr.wati.yacramanager.services;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.services.CustomUserDetailsService.CustomUserDetails;

/**
 * @author Rachid Ouattara
 * 
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<Users> {

	public Users getCurrentAuditor() {

		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
			return null;
		}

		return ((CustomUserDetails) authentication.getPrincipal())
				.getDomainUser();
	}
}
