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

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import fr.wati.yacramanager.beans.Role;
import fr.wati.yacramanager.services.security.PreAuthenticationChecker;
import fr.wati.yacramanager.web.filter.AjaxTimeoutRedirectFilter;

/**
 * Customizes Spring Security configuration.
 * 
 * @author Rob Winch
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String DEFAULT_LOGIN_SUCCESS_PATH = "/app/view/";
	@Autowired
	private Environment env;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PreAuthenticationChecker preAuthenticationChecker;

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
				.antMatchers("/auth/api/**")
				.permitAll()
				.antMatchers("/signin/**","/signup/**","/connect/**")
				.permitAll()
				.antMatchers("/app/admin/**")
				.hasAnyRole("ADMIN")
				.antMatchers("/app/**")
				.hasAnyRole(
						new String[] { "ADMIN", "SSII_ADMIN", "SALARIE",
								"INDEP" })
				.anyRequest()
				.authenticated()
				.and()
				.requiresChannel()
				.antMatchers("/**")
				.requiresSecure()
				.and()
				.formLogin()
					.defaultSuccessUrl(DEFAULT_LOGIN_SUCCESS_PATH)
					.loginProcessingUrl("/auth/authentication")
					.loginPage("/auth/")
					.failureUrl("/auth/?error=true")
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
						env.getProperty("rememberme.token.validity",
								Integer.class))
				.userDetailsService(userDetailsService)
				.and()
				.sessionManagement()
				// .invalidSessionUrl("/auth/login/?invalid-session=true")
				.maximumSessions(
						env.getProperty("max.sessions", Integer.class, 5))
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
		 web.ignoring()
         .antMatchers("/bower_components/**")
         .antMatchers("/fonts/**")
         .antMatchers("/templates/**")
         .antMatchers("/images/**")
         .antMatchers("/scripts/**")
         .antMatchers("/styles/**")
         .antMatchers("/i18n/**")
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
		ajaxTimeoutRedirectFilter.setCustomSessionExpiredErrorCode(env
				.getProperty("customSessionExpiredErrorCode", Integer.class));
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

	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPreAuthenticationChecks(preAuthenticationChecker);
		return authenticationProvider;
	}
}
