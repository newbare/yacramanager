package fr.wati.yacramanager.config;

import java.util.ArrayList;
import java.util.List;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.Resource;
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
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.wati.yacramanager.utils.CustomObjectMapper;


@Configuration
@EnableAspectJAutoProxy(proxyTargetClass= true)
@ComponentScan(basePackages = {"fr.wati.yacramanager.web"})
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter{
	@Autowired
	private CustomObjectMapper customObjectMapper;
	
	
	@Bean
    public ViewResolver viewResolver(ResourceLoader resourceLoader) {
		InternalResourceViewResolver  internalResourceViewResolver=new InternalResourceViewResolver();
		internalResourceViewResolver.setViewClass(JstlView.class);
		internalResourceViewResolver.setContentType("text/html;charset=UTF-8");
		internalResourceViewResolver.setPrefix("/views/");
		internalResourceViewResolver.setSuffix(".jsp");
        return internalResourceViewResolver;
    }

	@Bean
	public DozerBeanMapperFactoryBean dozerBeanMapper(ResourceLoader resourceLoader) {
		DozerBeanMapperFactoryBean dozerBeanMapper = new DozerBeanMapperFactoryBean();
		List<Resource> resources=new ArrayList<>();
		resources.add(resourceLoader.getResource("classpath:dozer-mapping.xml"));
		dozerBeanMapper.setMappingFiles(resources.toArray(new Resource[resources.size()]));
		return dozerBeanMapper;
	}
	
	@Bean
	public StandardServletMultipartResolver multipartResolver(){
		return new StandardServletMultipartResolver();
	}
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler("*/assets/**");
        resourceHandlerRegistration.addResourceLocations("/assets/");
        resourceHandlerRegistration.setCachePeriod(0);
    }

	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJacksonHttpMessageConverter.setObjectMapper(customObjectMapper);
		List<MediaType> mediaTypes=new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		mappingJacksonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
		converters.add(mappingJacksonHttpMessageConverter);
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}
