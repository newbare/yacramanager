package fr.wati.yacramanager.config;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
public class ThymeleafConfiguration  implements EnvironmentAware{

	@Inject
	private Environment env;
	
    private final Logger log = LoggerFactory.getLogger(ThymeleafConfiguration.class);

    private RelaxedPropertyResolver propertyResolver;
    
    /* (non-Javadoc)
	 * @see org.springframework.context.EnvironmentAware#setEnvironment(org.springframework.core.env.Environment)
	 */
	@Override
	public void setEnvironment(Environment environment) {
		this.propertyResolver = new RelaxedPropertyResolver(environment,
				"spring.thymeleaf.");
	}
    
    @Bean
	public ViewResolver appViewResolver(ResourceLoader resourceLoader) {
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setExcludedViewNames(new String[] { "*PDF" });
		thymeleafViewResolver.setTemplateEngine(templateEngine());
		thymeleafViewResolver.setCharacterEncoding("UTF-8");
		return thymeleafViewResolver;
	}


//	@Bean
//	public ViewResolver pdfViewResolver(ResourceLoader resourceLoader) {
//		ThymeleafPdfViewResolver thymeleafPdfViewResolver = new ThymeleafPdfViewResolver();
//		thymeleafPdfViewResolver.setViewNames(new String[] { "*PDF" });
//		thymeleafPdfViewResolver.setTemplateEngine(pdfTemplateEngine());
//		thymeleafPdfViewResolver.setCharacterEncoding("UTF-8");
//		return thymeleafPdfViewResolver;
//	}

	@Bean
	@Description("Thymeleaf template resolver serving HTML 5 app page")
	public ServletContextTemplateResolver webTemplateResolver() {
		ServletContextTemplateResolver webTemplateResolver = new ServletContextTemplateResolver();
		if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION,Constants.SPRING_PROFILE_TEST)) {
			webTemplateResolver.setPrefix("/dist/views/");
		}else {
			webTemplateResolver.setPrefix("/views/");
		}
		
		webTemplateResolver.setSuffix(".html");
		webTemplateResolver.setTemplateMode("LEGACYHTML5");
		webTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
		webTemplateResolver.setCacheable(propertyResolver.getProperty(
				"web.template.resolver.cache", Boolean.class, false));
		webTemplateResolver.setOrder(1);
		return webTemplateResolver;
	}

	@Bean
	@Description("Thymeleaf template resolver serving HTML 5 emails")
	public ClassLoaderTemplateResolver emailTemplateResolver() {
		ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
		emailTemplateResolver.setPrefix("mails/");
		emailTemplateResolver.setSuffix(".html");
		emailTemplateResolver.setTemplateMode("HTML5");
		emailTemplateResolver.setCacheable(propertyResolver.getProperty(
				"email.template.resolver.cache", Boolean.class, false));
		emailTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
		emailTemplateResolver.setOrder(2);
		return emailTemplateResolver;
	}

	@Bean
	@Description("Thymeleaf template resolver serving HTML 5 app page")
	public ServletContextTemplateResolver pdfTemplateResolver() {
		ServletContextTemplateResolver webTemplateResolver = new ServletContextTemplateResolver();
		webTemplateResolver.setPrefix("/pdf/");
		webTemplateResolver.setSuffix(".html");
		webTemplateResolver.setTemplateMode("XHTML");
		webTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
		webTemplateResolver.setCacheable(propertyResolver.getProperty(
				"pdf.template.resolver.cache", Boolean.class, false));
		webTemplateResolver.setOrder(3);
		return webTemplateResolver;
	}

	@Bean(name = "templateEngine")
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
		Set<ITemplateResolver> templateResolvers = new HashSet<>();
		templateResolvers.add(webTemplateResolver());
		templateResolvers.add(emailTemplateResolver());
		templateResolvers.add(pdfTemplateResolver());
		springTemplateEngine.setTemplateResolvers(templateResolvers);
		return springTemplateEngine;
	}

//	@Bean(name = "pdfTemplateEngine")
//	public SpringTemplateEngine pdfTemplateEngine() {
//		SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
//		Set<ITemplateResolver> templateResolvers = new HashSet<>();
//		templateResolvers.add(pdfTemplateResolver());
//		springTemplateEngine.setTemplateResolvers(templateResolvers);
//		return springTemplateEngine;
//	}

	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	
}
