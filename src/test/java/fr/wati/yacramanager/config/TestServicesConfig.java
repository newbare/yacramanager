package fr.wati.yacramanager.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = {"fr.wati.yacramanager.services"})
@PropertySource(value = { "classpath:database-yacra-test.properties" })
@Import(value={PersistenceConfig.class,MailConfiguration.class})
public class TestServicesConfig {

	@Autowired
	private Environment env;
	
	@Bean
	public DozerBeanMapperFactoryBean dozerBeanMapper(ResourceLoader resourceLoader) {
		DozerBeanMapperFactoryBean dozerBeanMapper = new DozerBeanMapperFactoryBean();
		List<Resource> resources=new ArrayList<>();
		resources.add(resourceLoader.getResource("classpath:dozer-mapping.xml"));
		dozerBeanMapper.setMappingFiles(resources.toArray(new Resource[resources.size()]));
		return dozerBeanMapper;
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(env.getProperty(
				"bcrypt.encoder.strength", Integer.class));
	}
	
	@Bean
	@Profile(value={Constants.SPRING_PROFILE_TEST})
	public DataSource dataSource()
	{
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		return dataSource;
	}
	
	@Bean
	@Profile(value={Constants.SPRING_PROFILE_TEST})
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
	{
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(Boolean.parseBoolean(env.getProperty("jpa.generateDdl")));
		vendorAdapter.setShowSql(Boolean.parseBoolean(env.getProperty("hibernate.show_sql")));
		vendorAdapter.setDatabasePlatform(env.getProperty("hibernate.dialect"));
		factory.setDataSource(dataSource());
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("fr.wati.yacramanager.beans");
		factory.getJpaPropertyMap().put("jadira.usertype.autoRegisterUserTypes", "true");
		factory.getJpaPropertyMap().put("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
		factory.getJpaPropertyMap().put("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
		factory.getJpaPropertyMap().put("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
		factory.getJpaPropertyMap().put("hibernate.cache.region.factory_class", env.getProperty("hibernate.cache.region.factory_class"));
		
		//factory.getJpaPropertyMap().put("hibernate.ejb.naming_strategy",CustomNamingStrategy.class.getName());
//		Properties jpaProperties = new Properties();
//		jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//		factory.setJpaProperties(jpaProperties);
		factory.afterPropertiesSet();
		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return factory;
	}
}
