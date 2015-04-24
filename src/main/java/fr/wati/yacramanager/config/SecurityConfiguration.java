/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.wati.yacramanager.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import fr.wati.yacramanager.web.filter.AjaxTimeoutRedirectFilter;

/**
 * Customizes Spring Security configuration.
 * 
 * @author Rob Winch
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
		implements EnvironmentAware {

	public static final String DEFAULT_LOGIN_SUCCESS_PATH = "/app/view/";

	private RelaxedPropertyResolver propertyResolver;
	@Inject
	private DataSource dataSource;

	@Inject
	private UserDetailsService userDetailsService;

	@Inject
	private UserDetailsChecker userDetailsChecker;

	private Environment environment;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
				.disable()
				// Refactor login form

				// See https://jira.springsource.org/browse/SPR-11496
				.headers()
				.addHeaderWriter(
						new XFrameOptionsHeaderWriter(
								XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))

				.and()
				.authorizeRequests()
				.antMatchers("/")
				.permitAll()
				.antMatchers("/auth/**", "/auth/api/**","/conf/**")
				.permitAll()
				.antMatchers("/signin/**", "/signup/**", "/connect/**")
				.permitAll()
				.antMatchers("/app/admin/**")
				.hasAnyRole("ADMIN")
				.antMatchers("/app/**")
				.hasAnyRole(
						new String[] { "ADMIN", "SSII_ADMIN", "SALARIE",
								"INDEP" }).anyRequest().authenticated();
		if (environment.acceptsProfiles(Constants.SPRING_PROFILE_CLOUD,
				Constants.SPRING_PROFILE_PRODUCTION,Constants.SPRING_PROFILE_TEST)) {
			http.requiresChannel().antMatchers("/**").requiresSecure();
		}
		http.formLogin()
				.defaultSuccessUrl(DEFAULT_LOGIN_SUCCESS_PATH)
				.loginProcessingUrl("/auth/authentication")
				.loginPage("/auth/login")
				.failureUrl("/auth/login?error=true")
				.permitAll()
				.and()
				.logout()
				.logoutUrl("/auth/logout")
				.logoutSuccessUrl("/?logout")
				.deleteCookies("JSESSIONID")
				.permitAll()
				.and()
				.httpBasic()
				.and()
				.rememberMe()
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(
						propertyResolver.getProperty(
								"rememberme.token.validity", Integer.class))
				.userDetailsService(userDetailsService)
				.and()
				.sessionManagement()
				// .invalidSessionUrl("/auth/login/?invalid-session=true")
				.maximumSessions(
						propertyResolver.getProperty("max.sessions",
								Integer.class, 5))
				.expiredUrl("/auth/?expired-session=true")
				.and()
				.and()
				.addFilterAfter(ajaxTimeoutRedirectFilter(),
						ExceptionTranslationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() {
		try {
			return super.authenticationManagerBean();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/bower_components/**")
				.antMatchers("/fonts/**").antMatchers("/templates/**")
				.antMatchers("/images/**").antMatchers("/scripts/**")
				.antMatchers("/styles/**").antMatchers("/i18n/**")
				.antMatchers("/swagger-ui/**");
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		return jdbcTokenRepositoryImpl;
	}

	@Bean
	public AjaxTimeoutRedirectFilter ajaxTimeoutRedirectFilter() {
		AjaxTimeoutRedirectFilter ajaxTimeoutRedirectFilter = new AjaxTimeoutRedirectFilter();
		ajaxTimeoutRedirectFilter
				.setCustomSessionExpiredErrorCode(propertyResolver.getProperty(
						"customSessionExpiredErrorCode", Integer.class));
		return ajaxTimeoutRedirectFilter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
	private static class GlobalSecurityConfiguration extends
			GlobalMethodSecurityConfiguration {
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPreAuthenticationChecks(userDetailsChecker);
		return authenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(propertyResolver.getProperty(
				"bcrypt.encoder.strength", Integer.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.EnvironmentAware#setEnvironment(org.
	 * springframework.core.env.Environment)
	 */
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
		this.propertyResolver = new RelaxedPropertyResolver(environment,
				"spring.security.");
	}
}
