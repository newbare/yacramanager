
package fr.wati.yacramanager.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import fr.wati.yacramanager.config.social.DefaultConnectionSignUp;
import fr.wati.yacramanager.config.social.SimpleSignInAdapter;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.UserService;
import fr.wati.yacramanager.utils.SecurityUtils;

/**
 * Spring Social Configuration.
 * @author Craig Walls
 */
@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

	@Inject
	private DataSource dataSource;
	
	@Inject
	private EmployeService employeService;
	@Inject
	private UserService userService;

	//
	// SocialConfigurer implementation methods
	//
	
	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
		cfConfig.addConnectionFactory(new FacebookConnectionFactory(env.getProperty("facebook.client.id"), env.getProperty("facebook.client.secret")));
		cfConfig.addConnectionFactory(new GitHubConnectionFactory(env.getProperty("git.client.id"), env.getProperty("git.client.secret")));
		cfConfig.addConnectionFactory(new TwitterConnectionFactory(env.getProperty("twitter.client.id"), env.getProperty("twitter.client.secret")));
	}


	/**
	 * Singleton data access object providing access to connections across all users.
	 */
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
		repository.setConnectionSignUp(defaultConnectionSignUp());
		return repository;
	}
	
	@Bean
	public DefaultConnectionSignUp defaultConnectionSignUp(){
		return new DefaultConnectionSignUp(employeService, userService);
	}
	
	public UserIdSource getUserIdSource() {
		return new UserIdSource() {
			@Override
			public String getUserId() {
				return SecurityUtils.getConnectedUser()!=null?SecurityUtils.getConnectedUser().getUsername():null;
			}
		};
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public Facebook facebook(ConnectionRepository repository) {
		Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
		return connection != null ? connection.getApi() : null;
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public Twitter twitter(ConnectionRepository repository) {
		Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
		return connection != null ? connection.getApi() : null;
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public GitHub gitHub(ConnectionRepository repository) {
		Connection<GitHub> connection = repository.findPrimaryConnection(GitHub.class);
		return connection != null ? connection.getApi() : null;
	}

	@Bean
	public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
		return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, new SimpleSignInAdapter(userService,WebSecurityConfig.DEFAULT_LOGIN_SUCCESS_PATH));
	}
	
	@Bean
	public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository){
		return new ConnectController(connectionFactoryLocator, connectionRepository);
	}

}