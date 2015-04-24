package fr.wati.yacramanager.config.social;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.userinfo.GoogleUserInfo;
import org.springframework.social.linkedin.api.CompanyOperations;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Gender;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.services.UserService;
import fr.wati.yacramanager.web.dto.RegistrationDTO;

/**
 * Simple little {@link ConnectionSignUp} command that allocates new userIds in
 * memory. Doesn't bother storing a user record in any local database, since
 * this quickstart just stores the user id in a cookie.
 * 
 * @author Keith Donald
 */
public class DefaultConnectionSignUp implements ConnectionSignUp {

	private final Logger log = LoggerFactory
			.getLogger(DefaultConnectionSignUp.class);

	private EmployeService employeService;
	private UserService userService;
	private SignUpDelegate gitHubDelegate = new GitHubConnectionSignUp();
	private SignUpDelegate facebookDelegate = new FacebookConnectionSignUp();
	private SignUpDelegate twitterDelegate = new TwitterConnectionSignUp();
	private SignUpDelegate googleDelegate = new GoogleConnectionSignUp();
	private SignUpDelegate linkedInDelegate = new LinkedinConnectionSignUp();

	public DefaultConnectionSignUp(EmployeService employeService,
			UserService userService) {
		super();
		this.employeService = employeService;
		this.userService = userService;
	}

	public EmployeService getEmployeService() {
		return employeService;
	}

	public UserService getUserService() {
		return userService;
	}

	public ConnectionSignUp getGitHubDelegate() {
		return gitHubDelegate;
	}

	public ConnectionSignUp getFacebookDelegate() {
		return facebookDelegate;
	}

	public ConnectionSignUp getTwitterDelegate() {
		return twitterDelegate;
	}

	public ConnectionSignUp getGoogleDelegate() {
		return googleDelegate;
	}

	public ConnectionSignUp getLinkedInDelegate() {
		return linkedInDelegate;
	}

	public String execute(Connection<?> connection) {
		Users existingUser = userService.findByEmail(connection
				.getDisplayName());
		if (existingUser == null) {
			switch (connection.getKey().getProviderId()) {
			case "facebook":
				return facebookDelegate.execute(connection);
			case "github":
				return gitHubDelegate.execute(connection);
			case "twitter":
				return twitterDelegate.execute(connection);
			case "google":
				return googleDelegate.execute(connection);
			case "linkedin":
				return linkedInDelegate.execute(connection);
			default:
				return null;
			}
		}
		return existingUser.getUserName();
	}

	public SignUpDelegate fromConnection(Connection<?> connection) {
		switch (connection.getKey().getProviderId()) {
		case "facebook":
			return facebookDelegate;
		case "github":
			return gitHubDelegate;
		case "twitter":
			return twitterDelegate;
		case "google":
			return googleDelegate;
		case "linkedin":
			return linkedInDelegate;
		default:
			return null;
		}
	}

	private class GitHubConnectionSignUp extends SignUpDelegate {

		@Override
		public RegistrationDTO fromConnection(Connection<?> connection) {
			GitHubUserProfile userProfile = ((GitHub) connection.getApi())
					.userOperations().getUserProfile();
			RegistrationDTO registrationDTO = new RegistrationDTO();
			registrationDTO.setUsername(userProfile.getUsername());
			registrationDTO.setFirstName(userProfile.getName());
			registrationDTO.setLastName(userProfile.getName());
			registrationDTO.setEmail(userProfile.getEmail());
			registrationDTO.setSocialUser(true);
			registrationDTO.setSocialProviderId(connection.getKey()
					.getProviderId());
			registrationDTO.setSocialUserId(connection.getKey()
					.getProviderUserId());
			registrationDTO.setProfileUrl(userProfile.getBlog());
			registrationDTO
					.setProfileImageUrl(userProfile.getProfileImageUrl());
			registrationDTO.setCompanyName(StringUtils.isEmpty(userProfile
					.getCompany()) ? userProfile.getUsername().toUpperCase()
					: userProfile.getCompany());
			return registrationDTO;
		}

	}

	private class FacebookConnectionSignUp extends SignUpDelegate {

		@Override
		public RegistrationDTO fromConnection(Connection<?> connection) {
			FacebookProfile userProfile = ((Facebook) connection.getApi())
					.userOperations().getUserProfile();
			RegistrationDTO registrationDTO = new RegistrationDTO();
			registrationDTO.setUsername(userProfile.getEmail());
			registrationDTO.setFirstName(userProfile.getName());
			registrationDTO.setLastName(userProfile.getName());
			registrationDTO.setEmail(userProfile.getEmail());
			registrationDTO.setSocialUser(true);
			registrationDTO
					.setGender("male".equals(userProfile.getGender()) ? Gender.HOMME
							: Gender.FEMME);
			registrationDTO.setBirthDay(DateTime.parse(userProfile
					.getBirthday()));
			registrationDTO.setSocialProviderId(connection.getKey()
					.getProviderId());
			registrationDTO.setSocialUserId(connection.getKey()
					.getProviderUserId());
			registrationDTO.setProfileUrl(userProfile.getLink());
			registrationDTO.setCompanyName(userProfile.getUsername()
					.toUpperCase());
			return registrationDTO;
		}

	}

	private class GoogleConnectionSignUp extends SignUpDelegate {

		@Override
		public RegistrationDTO fromConnection(Connection<?> connection) {
			GoogleUserInfo googleUserInfo = ((Google) connection.getApi())
					.userOperations().getUserInfo();
			RegistrationDTO registrationDTO = new RegistrationDTO();
			registrationDTO.setUsername(googleUserInfo.getEmail());
			registrationDTO.setFirstName(googleUserInfo.getFirstName());
			registrationDTO.setLastName(googleUserInfo.getLastName());
			registrationDTO.setEmail(googleUserInfo.getEmail());
			registrationDTO.setSocialUser(true);
			registrationDTO
					.setGender("male".equals(googleUserInfo.getGender()) ? Gender.HOMME
							: Gender.FEMME);
			registrationDTO.setSocialProviderId(connection.getKey()
					.getProviderId());
			registrationDTO.setSocialUserId(connection.getKey()
					.getProviderUserId());
			registrationDTO.setProfileUrl(googleUserInfo.getLink());
			registrationDTO.setProfileImageUrl(googleUserInfo
					.getProfilePictureUrl());
			registrationDTO.setCompanyName(googleUserInfo.getName()
					.toUpperCase());
			return registrationDTO;
		}

	}

	private class LinkedinConnectionSignUp extends SignUpDelegate {

		@Override
		public RegistrationDTO fromConnection(Connection<?> connection) {
			LinkedInProfileFull linkedInProfileFull = ((LinkedIn) connection
					.getApi()).profileOperations().getUserProfileFull();
			CompanyOperations companyOperations = ((LinkedIn) connection
					.getApi()).companyOperations();
			RegistrationDTO registrationDTO = new RegistrationDTO();
			registrationDTO.setUsername(linkedInProfileFull.getEmailAddress());
			registrationDTO.setFirstName(linkedInProfileFull.getFirstName());
			registrationDTO.setLastName(linkedInProfileFull.getLastName());
			registrationDTO.setEmail(linkedInProfileFull.getEmailAddress());
			// registrationDTO.setGender("male".equals(googleUserInfo.getGender())?Gender.HOMME:Gender.FEMME);
			registrationDTO.setSocialProviderId(connection.getKey()
					.getProviderId());
			registrationDTO.setSocialUser(true);
			registrationDTO.setSocialUserId(connection.getKey()
					.getProviderUserId());
			registrationDTO.setProfileUrl(linkedInProfileFull
					.getPublicProfileUrl());
			registrationDTO.setProfileImageUrl(linkedInProfileFull
					.getProfilePictureUrl());
			if (linkedInProfileFull.getDateOfBirth() != null) {
				registrationDTO.setBirthDay(new DateTime(linkedInProfileFull
						.getDateOfBirth().getYear(), linkedInProfileFull
						.getDateOfBirth().getMonth(), linkedInProfileFull
						.getDateOfBirth().getDay(), 0, 0));
			}

			// registrationDTO.setCompanyName(linkedInProfileFull.get);
			return registrationDTO;
		}

	}

	private class TwitterConnectionSignUp extends SignUpDelegate {

		@Override
		public RegistrationDTO fromConnection(Connection<?> connection) {
			Twitter twitter = ((Twitter) connection.getApi());
			TwitterProfile userProfile = twitter.userOperations()
					.getUserProfile();
			RegistrationDTO registrationDTO = new RegistrationDTO();
			// registrationDTO.setUsername(userProfile.getName());
			registrationDTO.setFirstName(userProfile.getScreenName());
			registrationDTO.setLastName(userProfile.getName());
			registrationDTO.setSocialUser(true);
			// registrationDTO.setEmail(userProfile.get);
			// registrationDTO.setCompanyName(userProfile.getUsername().toUpperCase());
			return registrationDTO;
		}

	}

	public abstract class SignUpDelegate implements ConnectionSignUp {
		@Override
		public String execute(Connection<?> connection) {
			Employe registerEmploye;
			try {
				registerEmploye = employeService.registerEmploye(
						fromConnection(connection), true);
				return registerEmploye.getUserName();
			} catch (ServiceException e) {
				log.error(e.getMessage(), e);
			}
			return null;
		}

		public abstract RegistrationDTO fromConnection(Connection<?> connection);
	}

}