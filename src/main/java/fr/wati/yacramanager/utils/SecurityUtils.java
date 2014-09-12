package fr.wati.yacramanager.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.services.CustomUserDetailsService.CustomUserDetails;
import fr.wati.yacramanager.services.impl.EmployeServiceImpl;

/**
 * @author Rachid-home
 */
@Component
public class SecurityUtils implements ApplicationContextAware{
	
	public static transient ApplicationContext applicationContext;
	
	public static boolean hasAnyRole(String... roles) {
		User user = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		for (String role : roles) {
			if(user.getAuthorities().contains(new SimpleGrantedAuthority(role))){
				return true;
			}
		}
		return false;
	}

	public static boolean hasAllRoles(String... roles) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		for (String role : roles) {
			if(!user.getAuthorities().contains(new SimpleGrantedAuthority(role))){
				return false;
			}
		}
		return true;
	}
	
	public static User getUser(){
		if(SecurityContextHolder.getContext().getAuthentication()==null || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
			return null;
		}
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user;
	}
	
	public static Employe getConnectedUser(){
		User user = getUser();
		if(user==null){
			return null;
		}
		return (user instanceof CustomUserDetails)?(Employe)((CustomUserDetails)user).getDomainUser():null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SecurityUtils.applicationContext=applicationContext;
	}
}
