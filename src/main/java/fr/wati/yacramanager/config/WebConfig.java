package fr.wati.yacramanager.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import fr.wati.yacramanager.config.apidoc.SwaggerConfiguration;
import fr.wati.yacramanager.services.CustomObjectMapper;
import fr.wati.yacramanager.web.thymeleaf.ThymeleafPdfViewResolver;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = { "fr.wati.yacramanager.web" })
@PropertySource(value = { "classpath:database-yacra.properties" })
@EnableWebMvc
@Import(value = { MetricsConfiguration.class, AspectConfiguration.class,
		LocaleConfiguration.class, ELFinderConfig.class,
		SwaggerConfiguration.class })
public class WebConfig extends WebMvcConfigurerAdapter {
	private Logger logger = LoggerFactory.getLogger(WebConfig.class);

	@Autowired
	private CustomObjectMapper customObjectMapper;

	@Autowired
	private Environment environment;


	@Bean
	public ViewResolver appViewResolver(ResourceLoader resourceLoader) {
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setExcludedViewNames(new String[] { "*PDF" });
		thymeleafViewResolver.setTemplateEngine(appTemplateEngine());
		thymeleafViewResolver.setCharacterEncoding("UTF-8");
		return thymeleafViewResolver;
	}


	@Bean
	public ViewResolver pdfViewResolver(ResourceLoader resourceLoader) {
		ThymeleafPdfViewResolver thymeleafPdfViewResolver = new ThymeleafPdfViewResolver();
		thymeleafPdfViewResolver.setViewNames(new String[] { "*PDF" });
		thymeleafPdfViewResolver.setTemplateEngine(pdfTemplateEngine());
		thymeleafPdfViewResolver.setCharacterEncoding("UTF-8");
		return thymeleafPdfViewResolver;
	}

	@Bean
	@Description("Thymeleaf template resolver serving HTML 5 app page")
	public ServletContextTemplateResolver webTemplateResolver() {
		ServletContextTemplateResolver webTemplateResolver = new ServletContextTemplateResolver();
		webTemplateResolver.setPrefix("/views/");
		webTemplateResolver.setSuffix(".html");
		webTemplateResolver.setTemplateMode("HTML5");
		webTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
		webTemplateResolver.setCacheable(environment.getProperty(
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
		emailTemplateResolver.setCacheable(environment.getProperty(
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
		webTemplateResolver.setCacheable(environment.getProperty(
				"pdf.template.resolver.cache", Boolean.class, false));
		webTemplateResolver.setOrder(3);
		return webTemplateResolver;
	}

	@Bean(name = "appTemplateEngine")
	public SpringTemplateEngine appTemplateEngine() {
		SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
		Set<ITemplateResolver> templateResolvers = new HashSet<>();
		templateResolvers.add(webTemplateResolver());
		templateResolvers.add(emailTemplateResolver());
		springTemplateEngine.setTemplateResolvers(templateResolvers);
		return springTemplateEngine;
	}

	@Bean(name = "pdfTemplateEngine")
	public SpringTemplateEngine pdfTemplateEngine() {
		SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
		Set<ITemplateResolver> templateResolvers = new HashSet<>();
		templateResolvers.add(pdfTemplateResolver());
		springTemplateEngine.setTemplateResolvers(templateResolvers);
		return springTemplateEngine;
	}

	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Integer cachePeriodInSecond = environment.getProperty("app.resources.cache.period", Integer.class, 0);
		registry.addResourceHandler("/bower_components/**").addResourceLocations("/bower_components/").setCachePeriod(cachePeriodInSecond);
		registry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/").setCachePeriod(cachePeriodInSecond);
		registry.addResourceHandler("/i18n/**").addResourceLocations("/i18n/").setCachePeriod(cachePeriodInSecond);
		registry.addResourceHandler("/styles/**").addResourceLocations("/styles/").setCachePeriod(cachePeriodInSecond);
        registry.addResourceHandler("/images/**").addResourceLocations("/images/").setCachePeriod(cachePeriodInSecond);
        registry.addResourceHandler("/scripts/**").addResourceLocations("/scripts/").setCachePeriod(cachePeriodInSecond);
        registry.addResourceHandler("/templates/**").addResourceLocations("/templates/").setCachePeriod(cachePeriodInSecond);
        registry.addResourceHandler("/views/**").addResourceLocations("/views/").setCachePeriod(cachePeriodInSecond);
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("/swagger-ui/").setCachePeriod(cachePeriodInSecond);
        
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
