package fr.wati.yacramanager.config.social;

import org.apache.commons.lang3.StringUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.UserService;
import fr.wati.yacramanager.web.dto.RegistrationDTO;

/**
 * Simple little {@link ConnectionSignUp} command that allocates new userIds in memory.
 * Doesn't bother storing a user record in any local database, since this quickstart just stores the user id in a cookie.
 * @author Keith Donald
 */
public class DefaultConnectionSignUp implements ConnectionSignUp {

	
	private EmployeService employeService;
	private UserService userService;
	
	
	
	public DefaultConnectionSignUp(EmployeService employeService,
			UserService userService) {
		super();
		this.employeService = employeService;
		this.userService = userService;
	}



	public String execute(Connection<?> connection) {
		Users existingUser = userService.findByUsername(connection.getDisplayName());
		if(existingUser==null){
			GitHubUserProfile userProfile = ((GitHub)connection.getApi()).userOperations().getUserProfile() ;
			RegistrationDTO registrationDTO=new RegistrationDTO();
			registrationDTO.setUsername(userProfile.getUsername());
			registrationDTO.setFirstName(userProfile.getName());
			registrationDTO.setLastName(userProfile.getName());
			registrationDTO.setEmail(userProfile.getEmail());
			registrationDTO.setCompanyName(StringUtils.isEmpty(userProfile.getCompany())? userProfile.getUsername().toUpperCase():userProfile.getCompany());
			Employe registerEmploye = employeService.registerEmploye(registrationDTO,true);
			return registerEmploye.getUsername();
		}
		return existingUser.getUsername();
	}

}