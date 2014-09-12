package fr.wati.yacramanager.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import fr.wati.yacramanager.services.CustomObjectMapper;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = { "fr.wati.yacramanager.web" })
@PropertySource(value = { "classpath:database-yacra.properties" })
@EnableWebMvc
@Import(value={MetricsConfiguration.class,AspectConfiguration.class})
public class WebConfig extends WebMvcConfigurerAdapter {
	private Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Autowired
	private CustomObjectMapper customObjectMapper;
	
	@Autowired
	private Environment  environment;

//	@Bean
//	public ViewResolver viewResolver(ResourceLoader resourceLoader) {
//		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
//		internalResourceViewResolver.setViewClass(JstlView.class);
//		internalResourceViewResolver.setContentType("text/html;charset=UTF-8");
//		internalResourceViewResolver.setPrefix("/views/");
//		internalResourceViewResolver.setSuffix(".jsp");
//		return internalResourceViewResolver;
//	}

	@Bean
	public ViewResolver viewResolver(ResourceLoader resourceLoader) {
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setTemplateEngine(templateEngine());
		thymeleafViewResolver.setCharacterEncoding("UTF-8");
		return thymeleafViewResolver;
	}
	
	@Bean
	@Description("Thymeleaf template resolver serving HTML 5 emails")
	public ServletContextTemplateResolver webTemplateResolver() {
		ServletContextTemplateResolver webTemplateResolver = new ServletContextTemplateResolver();
		webTemplateResolver.setPrefix("/views/");
		webTemplateResolver.setSuffix(".html");
		//webTemplateResolver.setTemplateMode("LEGACYHTML5");
		webTemplateResolver.setTemplateMode("HTML5");
		webTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
		webTemplateResolver.setCacheable(environment.getProperty("web.template.resolver.cache",Boolean.class,false));
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
		emailTemplateResolver.setCacheable(environment.getProperty("email.template.resolver.cache",Boolean.class,false));
		emailTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
		emailTemplateResolver.setOrder(2);
		return emailTemplateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(){
		SpringTemplateEngine springTemplateEngine=new SpringTemplateEngine();
		Set<ITemplateResolver> templateResolvers=new HashSet<>();
		templateResolvers.add(webTemplateResolver());
		templateResolvers.add(emailTemplateResolver());
		springTemplateEngine.setTemplateResolvers(templateResolvers);
		return springTemplateEngine;
	}	
	
	@Bean
	@Description("Spring mail message resolver")
	public MessageSource emailMessageSource() {
		logger.info("loading non-reloadable mail messages resources");
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/assets/mails/messages/messages");
		messageSource.setDefaultEncoding(CharEncoding.UTF_8);
		return messageSource;
	}

	
	@Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(environment.getProperty("cacheSeconds", Integer.class, 1));
        return messageSource;
    }
	
	

	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		ResourceHandlerRegistration resourceHandlerRegistration = registry
				.addResourceHandler("/assets/**");
		resourceHandlerRegistration.addResourceLocations("/assets/");
		resourceHandlerRegistration.setCachePeriod(0);
	}

	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJacksonHttpMessageConverter.setObjectMapper(customObjectMapper);
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		mappingJacksonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
		converters.add(mappingJacksonHttpMessageConverter);
	}

	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}
