package fr.wati.yacramanager.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.MetricsServlet;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Function;

import fr.wati.yacramanager.services.CustomObjectMapper;
import fr.wati.yacramanager.web.filter.CachingHttpHeadersFilter;
import fr.wati.yacramanager.web.filter.StaticResourcesProductionFilter;
import fr.wati.yacramanager.web.filter.gzip.GZipServletFilter;

@Configuration
@AutoConfigureAfter(CacheConfiguration.class)
public class WebConfiguration implements ServletContextInitializer,
		EmbeddedServletContainerCustomizer, EnvironmentAware {
	private Logger log = LoggerFactory.getLogger(WebConfiguration.class);

	@Inject
	private Environment env;

	@Autowired(required = false)
	private MetricRegistry metricRegistry;

	private RelaxedPropertyResolver propertyResolver;

	@Override
	public void setEnvironment(Environment env) {
		this.propertyResolver = new RelaxedPropertyResolver(env,
				"spring.webconfig.");
	}

	@Bean
	public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
		WebMvcConfigurerAdapter webMvcConfigurerAdapter = new WebMvcConfigurerAdapter() {
			
			/* (non-Javadoc)
			 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#configureContentNegotiation(org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer)
			 */
			@Override
			public void configureContentNegotiation(
					ContentNegotiationConfigurer configurer) {
				
			}

			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				Integer cachePeriodInSecond = propertyResolver.getProperty(
						"app.resources.cache.period", Integer.class, 0);
				registry.addResourceHandler("/bower_components/**")
						.addResourceLocations("/bower_components/")
						.setCachePeriod(cachePeriodInSecond);
				registry.addResourceHandler("/assets/fonts/**")
						.addResourceLocations("/assets/fonts/")
						.setCachePeriod(cachePeriodInSecond);
				registry.addResourceHandler("/i18n/**")
						.addResourceLocations("/i18n/")
						.setCachePeriod(cachePeriodInSecond);
				registry.addResourceHandler("/assets/styles/**")
						.addResourceLocations("/assets/styles/")
						.setCachePeriod(cachePeriodInSecond);
				registry.addResourceHandler("/assets/images/**")
						.addResourceLocations("/assets/images/")
						.setCachePeriod(cachePeriodInSecond);
				registry.addResourceHandler("/scripts/**")
						.addResourceLocations("/scripts/")
						.setCachePeriod(cachePeriodInSecond);
				registry.addResourceHandler("/templates/**")
						.addResourceLocations("/templates/")
						.setCachePeriod(cachePeriodInSecond);
				registry.addResourceHandler("/views/**")
						.addResourceLocations("/views/")
						.setCachePeriod(cachePeriodInSecond);
				registry.addResourceHandler("/swagger-ui/**")
						.addResourceLocations("/swagger-ui/")
						.setCachePeriod(cachePeriodInSecond);

			}
		};

		return webMvcConfigurerAdapter;
	}

	@Bean
	public HttpMessageConverters customConverters(JodaModule jacksonJodaModule) {
		MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJacksonHttpMessageConverter
				.setObjectMapper(new CustomObjectMapper(jacksonJodaModule));
//		mappingJacksonHttpMessageConverter
//				.setObjectMapper(new CustomObjectMapper());
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		mappingJacksonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
		return new HttpMessageConverters(mappingJacksonHttpMessageConverter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer
	 * #customize(org.springframework.boot.context.embedded.
	 * ConfigurableEmbeddedServletContainer)
	 */
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
		// IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
		mappings.add("html", "text/html;charset=utf-8");
		// CloudFoundry issue, see
		// https://github.com/cloudfoundry/gorouter/issues/64
		mappings.add("json", "text/html;charset=utf-8");
		container.setMimeMappings(mappings);
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
				"/views/404.html"));
		container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,
				"/views/500.html"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.boot.context.embedded.ServletContextInitializer#onStartup
	 * (javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		log.info("Web application configuration, using profiles: {}",
				Arrays.toString(env.getActiveProfiles()));
		EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST,
				DispatcherType.FORWARD, DispatcherType.ASYNC);
		if (!env.acceptsProfiles(Constants.SPRING_PROFILE_FAST)) {
			initMetrics(servletContext, disps);
		}
		if (env.acceptsProfiles(Constants.SPRING_PROFILE_HOMER)){
			log.debug("Registering static resources production Filter");
			FilterRegistration.Dynamic staticResourcesProductionFilter = servletContext
					.addFilter("staticResourcesHomerThemeFilter",
							new StaticResourcesProductionFilter(new Function<String, String>() {
								@Override
								public String apply(String requestURI) {
									return "/homer" + requestURI;
								}
							}));
			staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
					"/assets/*");
			staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
					"/scripts/*");
			staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
					"/views/*");
			staticResourcesProductionFilter.setAsyncSupported(true);
		}
		if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION,
				Constants.SPRING_PROFILE_TEST)) {
			initCachingHttpHeadersFilter(servletContext, disps);
			initStaticResourcesProductionFilter(servletContext, disps);
			initGzipFilter(servletContext, disps);
		}
		log.info("Web application fully configured");
	}

	/**
	 * Initializes Metrics.
	 */
	private void initMetrics(ServletContext servletContext,
			EnumSet<DispatcherType> disps) {
		log.debug("Initializing Metrics registries");
		servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE,
				metricRegistry);
		servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY,
				metricRegistry);

		log.debug("Registering Metrics Filter");
		FilterRegistration.Dynamic metricsFilter = servletContext.addFilter(
				"webappMetricsFilter", new InstrumentedFilter());

		metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
		metricsFilter.setAsyncSupported(true);

		log.debug("Registering Metrics Servlet");
		ServletRegistration.Dynamic metricsAdminServlet = servletContext
				.addServlet("metricsServlet", new MetricsServlet());

		metricsAdminServlet.addMapping("/app/admin/metrics");
		metricsAdminServlet.setAsyncSupported(true);
		metricsAdminServlet.setLoadOnStartup(2);

		// log.debug("Registering HealthCheck Servlet");
		// ServletRegistration.Dynamic healthCheckAdminServlet = servletContext
		// .addServlet("healthCheckServlet",
		// new HealthCheckCustomServlet());
		//
		// healthCheckAdminServlet.addMapping("/app/admin/health");
		// healthCheckAdminServlet.setAsyncSupported(true);
		// healthCheckAdminServlet.setLoadOnStartup(3);
	}

	/**
	 * Initializes the static resources production Filter.
	 */
	private void initStaticResourcesProductionFilter(
			ServletContext servletContext, EnumSet<DispatcherType> disps) {

		log.debug("Registering static resources production Filter");
		FilterRegistration.Dynamic staticResourcesProductionFilter = servletContext
				.addFilter("staticResourcesProductionFilter",
						new StaticResourcesProductionFilter(new Function<String, String>() {
							@Override
							public String apply(String requestURI) {
								return "/dist" + requestURI;
							}
						}));
		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
				"/assets/*");
		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
				"/scripts/*");
		staticResourcesProductionFilter.setAsyncSupported(true);
	}

	/**
	 * Initializes the GZip filter.
	 */
	private void initGzipFilter(ServletContext servletContext,
			EnumSet<DispatcherType> disps) {
		log.debug("Registering GZip Filter");
		FilterRegistration.Dynamic compressingFilter = servletContext
				.addFilter("gzipFilter", new GZipServletFilter());
		Map<String, String> parameters = new HashMap<>();
		compressingFilter.setInitParameters(parameters);
		compressingFilter.addMappingForUrlPatterns(disps, true, "*.css");
		compressingFilter.addMappingForUrlPatterns(disps, true, "*.json");
		compressingFilter.addMappingForUrlPatterns(disps, true, "*.html");
		compressingFilter.addMappingForUrlPatterns(disps, true, "*.js");
		compressingFilter.addMappingForUrlPatterns(disps, true, "*.svg");
		compressingFilter.addMappingForUrlPatterns(disps, true, "*.ttf");
		compressingFilter.addMappingForUrlPatterns(disps, true, "/api/*");
		compressingFilter.addMappingForUrlPatterns(disps, true, "/metrics/*");
		compressingFilter.setAsyncSupported(true);
	}

	/**
	 * Initializes the cachig HTTP Headers Filter.
	 */
	private void initCachingHttpHeadersFilter(ServletContext servletContext,
			EnumSet<DispatcherType> disps) {
		log.debug("Registering Caching HTTP Headers Filter");
		FilterRegistration.Dynamic cachingHttpHeadersFilter = servletContext
				.addFilter("cachingHttpHeadersFilter",
						new CachingHttpHeadersFilter());

		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true,
				"/assets/*");
		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true,
				"/scripts/*");
		cachingHttpHeadersFilter.setAsyncSupported(true);
	}

}
