/**
 * 
 */
package fr.wati.yacramanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;

/**
 * @author Rachid Ouattara
 * 
 */
@Configuration
public class ViewsConfiguration {

	@Bean
	public ResourceBundleViewResolver resourceBundleViewResolver() {
		ResourceBundleViewResolver resolver = new ResourceBundleViewResolver();
		resolver.setBasename("spring-mvc-views");
		return resolver;
	}
}
