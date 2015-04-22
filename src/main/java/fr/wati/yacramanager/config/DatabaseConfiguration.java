package fr.wati.yacramanager.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate3.HibernateExceptionTranslator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr.wati.yacramanager.dao.JdbcCompanyInvitationRepository;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "fr.wati.yacramanager.dao")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration implements EnvironmentAware {
	private Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

	private RelaxedPropertyResolver propertyResolver;
	
	private Environment env;

	@Autowired(required = false)
	private MetricRegistry metricRegistry;

	@Override
	public void setEnvironment(Environment env) {
		this.env=env;
		this.propertyResolver = new RelaxedPropertyResolver(env,
				"spring.datasource.");
	}

//	@Bean
//	public PlatformTransactionManager transactionManager() {
//		EntityManagerFactory factory = entityManagerFactory().getObject();
//		return new JpaTransactionManager(factory);
//	}

//	@Bean
//	@Profile(value = { Constants.SPRING_PROFILE_DEVELOPMENT,
//			Constants.SPRING_PROFILE_PRODUCTION })
//	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//		vendorAdapter.setGenerateDdl(Boolean.parseBoolean(propertyResolver
//				.getProperty("jpa.generateDdl")));
//		vendorAdapter.setShowSql(Boolean.parseBoolean(propertyResolver
//				.getProperty("hibernate.show_sql")));
//		vendorAdapter.setDatabasePlatform(propertyResolver
//				.getProperty("hibernate.dialect"));
//		factory.setDataSource(dataSource());
//		factory.setJpaVendorAdapter(vendorAdapter);
//		factory.setPackagesToScan("fr.wati.yacramanager.beans");
//		factory.getJpaPropertyMap().put(
//				"jadira.usertype.autoRegisterUserTypes", "true");
//		factory.getJpaPropertyMap().put(
//				"hibernate.cache.use_second_level_cache",
//				propertyResolver
//						.getProperty("hibernate.cache.use_second_level_cache"));
//		factory.getJpaPropertyMap()
//				.put("hibernate.cache.use_query_cache",
//						propertyResolver
//								.getProperty("hibernate.cache.use_query_cache"));
//		factory.getJpaPropertyMap().put("hibernate.generate_statistics",
//				propertyResolver.getProperty("hibernate.generate_statistics"));
//		factory.getJpaPropertyMap().put(
//				"hibernate.cache.region.factory_class",
//				propertyResolver
//						.getProperty("hibernate.cache.region.factory_class"));
//
//		// factory.getJpaPropertyMap().put("hibernate.ejb.naming_strategy",CustomNamingStrategy.class.getName());
//		// Properties jpaProperties = new Properties();
//		// jpaProperties.put("hibernate.hbm2ddl.auto",
//		// env.getProperty("hibernate.hbm2ddl.auto"));
//		// factory.setJpaProperties(jpaProperties);
//		factory.afterPropertiesSet();
//		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
//		return factory;
//	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	@Bean(destroyMethod = "shutdown")
	@Profile(value = { Constants.SPRING_PROFILE_DEVELOPMENT,
			Constants.SPRING_PROFILE_PRODUCTION,
			"!" + Constants.SPRING_PROFILE_CLOUD })
	@ConditionalOnMissingClass(name = "fr.wati.payme.webapp.config.HerokuDatabaseConfiguration")
	public DataSource dataSource() {
		log.debug("Configuring Datasource");
		if (propertyResolver.getProperty("url") == null
				&& propertyResolver.getProperty("databaseName") == null) {
			log.error(
					"Your database connection pool configuration is incorrect! The application"
							+ "cannot start. Please check your Spring profile, current profiles are: {}",
					Arrays.toString(env.getActiveProfiles()));

			throw new ApplicationContextException(
					"Database connection pool is not configured correctly");
		}
		HikariConfig config = new HikariConfig();
		config.setDataSourceClassName(propertyResolver
				.getProperty("dataSourceClassName"));
		if (propertyResolver.getProperty("url") == null
				|| "".equals(propertyResolver.getProperty("url"))) {
			config.addDataSourceProperty("databaseName",
					propertyResolver.getProperty("databaseName"));
			config.addDataSourceProperty("serverName",
					propertyResolver.getProperty("serverName"));
		} else {
			config.addDataSourceProperty("url",
					propertyResolver.getProperty("url"));
		}
		config.addDataSourceProperty("user",
				propertyResolver.getProperty("username"));
		config.addDataSourceProperty("password",
				propertyResolver.getProperty("password"));

		if (metricRegistry != null) {
			config.setMetricRegistry(metricRegistry);
		}
		return new HikariDataSource(config);
	}

	@Bean
	public JdbcCompanyInvitationRepository companyInvitationRepository(){
		return new JdbcCompanyInvitationRepository(dataSource());
	}
	
	@Bean(name="customDataSourceInitializer")
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.setContinueOnError(true);
		databasePopulator.addScript(new ClassPathResource(
				"sql/spring-security-persistent-login.sql"));
		databasePopulator.addScript(new ClassPathResource(
				"sql/roles-initialization.sql"));
		databasePopulator.addScript(new ClassPathResource(
				"sql/company-invitation-create.sql"));
		databasePopulator.addScript(new ClassPathResource(
				"sql/spring-social-userconnection.sql"));
		databasePopulator.addScript(new ClassPathResource(
				"sql/create-admin-user.sql"));
		dataSourceInitializer.setDatabasePopulator(databasePopulator);
		dataSourceInitializer.setEnabled(propertyResolver.getProperty(
				"init-db", Boolean.class, false));
		return dataSourceInitializer;
	}

	@Bean
	public Hibernate4Module hibernate4Module() {
		return new Hibernate4Module();
	}
}