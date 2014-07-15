package fr.wati.yacramanager.config;

import java.util.ArrayList;
import java.util.List;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
@ComponentScan(basePackages = {"fr.wati.yacramanager.services"})
@PropertySource(value = { "classpath:database-yacra-test.properties" })
@Import(value={PersistenceConfig.class})
public class TestServicesConfig {

	
	@Bean
	public DozerBeanMapperFactoryBean dozerBeanMapper(ResourceLoader resourceLoader) {
		DozerBeanMapperFactoryBean dozerBeanMapper = new DozerBeanMapperFactoryBean();
		List<Resource> resources=new ArrayList<>();
		resources.add(resourceLoader.getResource("classpath:dozer-mapping.xml"));
		dozerBeanMapper.setMappingFiles(resources.toArray(new Resource[resources.size()]));
		return dozerBeanMapper;
	}
}