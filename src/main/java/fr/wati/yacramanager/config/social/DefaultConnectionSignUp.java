package fr.wati.yacramanager.config.social;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.userinfo.GoogleUserInfo;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Gender;
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
	private ConnectionSignUp gitHubDelegate=new GitHubConnectionSignUp();
	private ConnectionSignUp facebookDelegate=new FacebookConnectionSignUp();
	private ConnectionSignUp twitterDelegate=new TwitterConnectionSignUp();
	private ConnectionSignUp googleDelegate=new GoogleConnectionSignUp();
	
	
	
	public DefaultConnectionSignUp(EmployeService employeService,
			UserService userService) {
		super();
		this.employeService = employeService;
		this.userService = userService;
	}



	public String execute(Connection<?> connection) {
		Users existingUser = userService.findByUsername(connection.getDisplayName());
		if(existingUser==null){
			switch (connection.getKey().getProviderId()) {
			case "facebook":
				return facebookDelegate.execute(connection);
			case "github":
				return gitHubDelegate.execute(connection);
			case "twitter":
				return twitterDelegate.execute(connection);
			case "google":
				return googleDelegate.execute(connection);
			default:
				return null;
			}
		}
		return existingUser.getUsername();
	}
	
	private class GitHubConnectionSignUp implements ConnectionSignUp{

		@Override
		public String execute(Connection<?> connection) {
			GitHubUserProfile userProfile = ((GitHub)connection.getApi()).userOperations().getUserProfile() ;
			RegistrationDTO registrationDTO=new RegistrationDTO();
			registrationDTO.setUsername(userProfile.getUsername());
			registrationDTO.setFirstName(userProfile.getName());
			registrationDTO.setLastName(userProfile.getName());
			registrationDTO.setEmail(userProfile.getEmail());
			registrationDTO.setSocialProviderId(connection.getKey().getProviderId());
			registrationDTO.setSocialUserId(connection.getKey().getProviderUserId());
			registrationDTO.setProfileUrl(userProfile.getBlog());
			registrationDTO.setProfileImageUrl(userProfile.getProfileImageUrl());
			registrationDTO.setCompanyName(StringUtils.isEmpty(userProfile.getCompany())? userProfile.getUsername().toUpperCase():userProfile.getCompany());
			Employe registerEmploye = employeService.registerEmploye(registrationDTO,true);
			return registerEmploye.getUsername();
		}
		
	}
	
	private class FacebookConnectionSignUp implements ConnectionSignUp{

		@Override
		public String execute(Connection<?> connection) {
			FacebookProfile userProfile = ((Facebook)connection.getApi()).userOperations().getUserProfile() ;
			RegistrationDTO registrationDTO=new RegistrationDTO();
			registrationDTO.setUsername(userProfile.getUsername());
			registrationDTO.setFirstName(userProfile.getName());
			registrationDTO.setLastName(userProfile.getName());
			registrationDTO.setEmail(userProfile.getEmail());
			registrationDTO.setGender("male".equals(userProfile.getGender())?Gender.HOMME:Gender.FEMME);
			registrationDTO.setBirthDay(DateTime.parse(userProfile.getBirthday()));
			registrationDTO.setSocialProviderId(connection.getKey().getProviderId());
			registrationDTO.setSocialUserId(connection.getKey().getProviderUserId());
			registrationDTO.setProfileUrl(userProfile.getLink());
			registrationDTO.setCompanyName(userProfile.getUsername().toUpperCase());
			Employe registerEmploye = employeService.registerEmploye(registrationDTO,true);
			return registerEmploye.getUsername();
		}
		
	}
	
	private class GoogleConnectionSignUp implements ConnectionSignUp{

		@Override
		public String execute(Connection<?> connection) {
			GoogleUserInfo googleUserInfo = ((Google)connection.getApi()).userOperations().getUserInfo() ;
			RegistrationDTO registrationDTO=new RegistrationDTO();
			registrationDTO.setUsername(googleUserInfo.getId());
			registrationDTO.setFirstName(googleUserInfo.getFirstName());
			registrationDTO.setLastName(googleUserInfo.getLastName());
			registrationDTO.setEmail(googleUserInfo.getEmail());
			registrationDTO.setGender("male".equals(googleUserInfo.getGender())?Gender.HOMME:Gender.FEMME);
			registrationDTO.setSocialProviderId(connection.getKey().getProviderId());
			registrationDTO.setSocialUserId(connection.getKey().getProviderUserId());
			registrationDTO.setProfileUrl(googleUserInfo.getLink());
			registrationDTO.setProfileImageUrl(googleUserInfo.getProfilePictureUrl());
			registrationDTO.setCompanyName(googleUserInfo.getName().toUpperCase());
			Employe registerEmploye = employeService.registerEmploye(registrationDTO,true);
			return registerEmploye.getUsername();
		}
		
	}
	
	private class TwitterConnectionSignUp implements ConnectionSignUp{

		@Override
		public String execute(Connection<?> connection) {
			Twitter twitter=((Twitter)connection.getApi());
			TwitterProfile userProfile = twitter.userOperations().getUserProfile() ;
			RegistrationDTO registrationDTO=new RegistrationDTO();
			registrationDTO.setUsername(userProfile.getName());
			registrationDTO.setFirstName(userProfile.getScreenName());
			registrationDTO.setLastName(userProfile.getName());
//			registrationDTO.setEmail(userProfile.get);
//			registrationDTO.setCompanyName(userProfile.getUsername().toUpperCase());
			Employe registerEmploye = employeService.registerEmploye(registrationDTO,true);
			return registerEmploye.getUsername();
		}
		
	}

}