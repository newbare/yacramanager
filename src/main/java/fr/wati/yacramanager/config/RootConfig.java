package fr.wati.yacramanager.config;

import java.util.ArrayList;
import java.util.List;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = { "fr.wati.yacramanager.dao",
		"fr.wati.yacramanager.services", "fr.wati.yacramanager.listeners" })
@PropertySource(value = { "classpath:database-yacra.properties" })
@Import(value = { PersistenceConfig.class, MailConfiguration.class,
		AsyncConfiguration.class,WebSecurityConfig.class,CacheConfiguration.class })
public class RootConfig {

	@Autowired
	private Environment  environment;
	
	@Bean
	public DozerBeanMapperFactoryBean dozerBeanMapper(
			ResourceLoader resourceLoader) {
		DozerBeanMapperFactoryBean dozerBeanMapper = new DozerBeanMapperFactoryBean();
		List<Resource> resources = new ArrayList<>();
		resources
				.add(resourceLoader.getResource("classpath:dozer-mapping.xml"));
		dozerBeanMapper.setMappingFiles(resources
				.toArray(new Resource[resources.size()]));
		return dozerBeanMapper;
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(environment.getProperty(
				"bcrypt.encoder.strength", Integer.class));
	}
}
