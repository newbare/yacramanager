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
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

/**
 * Customizes Spring Security configuration.
 * 
 * @author Rob Winch
 */
@Configuration
@EnableWebSecurity
@Import({ PersistenceConfig.class })
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private PasswordEncoder passwordEncoder;

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
				.formLogin()
				.defaultSuccessUrl("/")
				.loginPage("/login")
				.failureUrl("/login?error=true")
				.permitAll()
				.and()
				.logout()
				.permitAll()
				.and()
				.authorizeRequests()
				.antMatchers("/resources/**")
				.permitAll()
				.antMatchers("/assets/**")
				.permitAll()
				.and()
				.rememberMe()
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(
						env.getProperty("rememberme.token.validity",
								Integer.class))
				.and()
				.authorizeRequests()
				.antMatchers("/**")
				.hasAnyRole(
						new String[] { "ADMIN", "SSII_ADMIN", "SALARIE",
								"INDEP" }).and().authorizeRequests()
				.anyRequest().authenticated();
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		return jdbcTokenRepositoryImpl;
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(env.getProperty(
				"bcrypt.encoder.strength", Integer.class));
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.authoritiesByUsernameQuery(
						"SELECT u.USERNAME, r.ROLE FROM USERs u, USERs_ROLEs ur,ROLE r WHERE u.ID = ur.userId and r.id=ur.roleId AND u.USERNAME=?;")
				.usersByUsernameQuery(
						"SELECT USERNAME, PASSWORD, ENABLED FROM USERs WHERE USERNAME=?;")
				.passwordEncoder(passwordEncoder);

	}
}