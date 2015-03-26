package fr.wati.yacramanager.config.social;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

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
		UserProfile userProfile = connection.fetchUserProfile();
		Users existingUser = userService.findByUsername(userProfile.getUsername());
		if(existingUser==null){
			RegistrationDTO registrationDTO=new RegistrationDTO();
			registrationDTO.setUsername(userProfile.getUsername());
			registrationDTO.setFirstName(userProfile.getFirstName());
			registrationDTO.setLastName(userProfile.getLastName());
			registrationDTO.setEmail(userProfile.getEmail());
			registrationDTO.setCompanyName(userProfile.getUsername().toUpperCase());
			Employe registerEmploye = employeService.registerEmploye(registrationDTO,true);
			return registerEmploye.getUsername();
		}
		return existingUser.getUsername();
	}

}