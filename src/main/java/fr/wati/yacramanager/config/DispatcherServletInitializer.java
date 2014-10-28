/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.wati.yacramanager.config;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;

import fr.wati.yacramanager.config.metrics.HealthCheckCustomServlet;
import fr.wati.yacramanager.web.filter.CachingHttpHeadersFilter;
import fr.wati.yacramanager.web.filter.StaticResourcesProductionFilter;
import fr.wati.yacramanager.web.filter.gzip.GZipServletFilter;

public class DispatcherServletInitializer extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	private final Logger log = LoggerFactory
			.getLogger(DispatcherServletInitializer.class);

	private int maxUploadSizeInMb = 5 * 1024 * 1024; // 5 MB

	private WebApplicationContext servletAppContext;

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { RootConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class, WebSocketConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setInitParameter("dispatchOptionsRequest", "true");
		// File uploadDirectory = new
		// File(ServiceConfiguration.FILE_UPLOAD_PATH);
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
				null, maxUploadSizeInMb, maxUploadSizeInMb * 2,
				maxUploadSizeInMb / 2);
		registration.setMultipartConfig(multipartConfigElement);
	}

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		super.onStartup(servletContext);
		EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST,
				DispatcherType.FORWARD, DispatcherType.ASYNC);
		initMetrics(servletContext, disps);
		if (servletAppContext.getEnvironment().acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
			initCachingHttpHeadersFilter(servletContext, disps);
			initStaticResourcesProductionFilter(servletContext, disps);
		}
		initGzipFilter(servletContext, disps);
		log.info("Web application fully configured");
	}
	
	protected void registerDispatcherServlet(ServletContext servletContext) {
		String servletName = getServletName();
		Assert.hasLength(servletName, "getServletName() may not return empty or null");

		servletAppContext = createServletApplicationContext();
		Assert.notNull(servletAppContext,
				"createServletApplicationContext() did not return an application " +
				"context for servlet [" + servletName + "]");

		DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
		ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, dispatcherServlet);
		Assert.notNull(registration,
				"Failed to register servlet with name '" + servletName + "'." +
				"Check if there is another servlet registered under the same name.");

		registration.setLoadOnStartup(1);
		registration.addMapping(getServletMappings());
		registration.setAsyncSupported(isAsyncSupported());

		Filter[] filters = getServletFilters();
		if (!ObjectUtils.isEmpty(filters)) {
			for (Filter filter : filters) {
				registerServletFilter(servletContext, filter);
			}
		}

		customizeRegistration(registration);
	}

	/**
	 * Initializes the cachig HTTP Headers Filter.
	 */
	private void initCachingHttpHeadersFilter(ServletContext servletContext,
			EnumSet<DispatcherType> disps) {
		log.debug("Registering Cachig HTTP Headers Filter");
		FilterRegistration.Dynamic cachingHttpHeadersFilter = servletContext
				.addFilter("cachingHttpHeadersFilter",
						new CachingHttpHeadersFilter());

		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true,
				"/images/*");
		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true,
				"/fonts/*");
		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true,
				"/scripts/*");
		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true,
				"/styles/*");
		cachingHttpHeadersFilter.setAsyncSupported(true);
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
		compressingFilter.addMappingForUrlPatterns(disps, true, "/app/api/*");
		compressingFilter.addMappingForUrlPatterns(disps, true, "/metrics/*");
		compressingFilter.setAsyncSupported(true);
	}

	/**
	 * Initializes the static resources production Filter.
	 */
	private void initStaticResourcesProductionFilter(
			ServletContext servletContext, EnumSet<DispatcherType> disps) {

		log.debug("Registering static resources production Filter");
		FilterRegistration.Dynamic staticResourcesProductionFilter = servletContext
				.addFilter("staticResourcesProductionFilter",
						new StaticResourcesProductionFilter());

		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
				"/");
		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
				"/index.html");
		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
				"/images/*");
		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
				"/fonts/*");
		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
				"/scripts/*");
		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
				"/styles/*");
		staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true,
				"/views/*");
		staticResourcesProductionFilter.setAsyncSupported(true);
	}

	/**
	 * Initializes Metrics.
	 */
	private void initMetrics(ServletContext servletContext,
			EnumSet<DispatcherType> disps) {
		log.debug("Initializing Metrics registries");
		servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE,
				MetricsConfiguration.METRIC_REGISTRY);
		servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY,
				MetricsConfiguration.METRIC_REGISTRY);

		servletContext.setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY,
				MetricsConfiguration.HEALTH_CHECK_REGISTRY);

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

		log.debug("Registering HealthCheck Servlet");
		ServletRegistration.Dynamic healthCheckAdminServlet = servletContext
				.addServlet("healthCheckServlet",
						new HealthCheckCustomServlet());

		healthCheckAdminServlet.addMapping("/app/admin/health");
		healthCheckAdminServlet.setAsyncSupported(true);
		healthCheckAdminServlet.setLoadOnStartup(3);
	}

}
