
package fr.wati.yacramanager.config.social;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.services.UserService;
import fr.wati.yacramanager.services.security.CustomUserDetailsService.CustomUserDetails;
import fr.wati.yacramanager.utils.SecurityContext;

/**
 * Signs the user in by setting the currentUser property on the {@link SecurityContext}.
 * Remembers the sign-in after the current request completes by storing the user's id in a cookie.
 * This is cookie is read in {@link UserInterceptor#preHandle(HttpServletRequest, HttpServletResponse, Object)} on subsequent requests.
 * @author Keith Donald
 * @see UserInterceptor
 */
public final class SimpleSignInAdapter implements SignInAdapter {

	private UserService userService;
	private String defaultLoginSuccessUrl;
	
	
	
	public SimpleSignInAdapter(UserService userService,String defaultLoginSuccessUrl) {
		super();
		this.userService = userService;
		this.defaultLoginSuccessUrl=defaultLoginSuccessUrl;
	}



	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		Users existingUser = userService.findByUsername(userId);
		if(existingUser!=null){
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
			=new UsernamePasswordAuthenticationToken(existingUser.getUsername(), null, CustomUserDetails.getGrantedAuthorities(existingUser.getRoles()));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			return defaultLoginSuccessUrl;
		}
		
		return null;
	}

}