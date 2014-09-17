/**
 * 
 */
package fr.wati.yacramanager.config.metrics;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ExecutorService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.json.HealthCheckModule;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
public class HealthCheckCustomServlet extends HealthCheckServlet {

	 private static final String CONTENT_TYPE = "application/json";
	 private transient ObjectMapper mapper;
	 private transient ExecutorService executorService;
	 private transient HealthCheckRegistry registry;
	 
	 
	 
	/**
	 * 
	 */
	public HealthCheckCustomServlet() {
		super();
	}

	/**
	 * @param registry
	 */
	public HealthCheckCustomServlet(HealthCheckRegistry registry) {
		super(registry);
	}

	
	 @Override
	    public void init(ServletConfig config) throws ServletException {
	        super.init(config);

	        if (null == registry) {
	            final Object registryAttr = config.getServletContext().getAttribute(HEALTH_CHECK_REGISTRY);
	            if (registryAttr instanceof HealthCheckRegistry) {
	                this.registry = (HealthCheckRegistry) registryAttr;
	            } else {
	                throw new ServletException("Couldn't find a HealthCheckRegistry instance.");
	            }
	        }

	        final Object executorAttr = config.getServletContext().getAttribute(HEALTH_CHECK_EXECUTOR);
	        if (executorAttr instanceof ExecutorService) {
	            this.executorService = (ExecutorService) executorAttr;
	        }

	        this.mapper = new ObjectMapper().registerModule(new HealthCheckModule());
	    }
	
	/* (non-Javadoc)
	 * @see com.codahale.metrics.servlets.HealthCheckServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final SortedMap<String, HealthCheck.Result> results = runHealthChecks();
        resp.setContentType(CONTENT_TYPE);
        resp.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
        if (results.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        } else {
        	/*
        	 * Always return HttpServletResponse.SC_OK(200) as error must be in the response
        	 */
            if (isAllHealthy(results)) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }

        final OutputStream output = resp.getOutputStream();
        try {
            getWriter(req).writeValue(output, results);
        } finally {
            output.close();
        }
	}

	private ObjectWriter getWriter(HttpServletRequest request) {
        final boolean prettyPrint = Boolean.parseBoolean(request.getParameter("pretty"));
        if (prettyPrint) {
            return mapper.writerWithDefaultPrettyPrinter();
        }
        return mapper.writer();
    }
	
	private SortedMap<String, HealthCheck.Result> runHealthChecks() {
        if (executorService == null) {
            return registry.runHealthChecks();
        }
        return registry.runHealthChecks(executorService);
    }

    private static boolean isAllHealthy(Map<String, HealthCheck.Result> results) {
        for (HealthCheck.Result result : results.values()) {
            if (!result.isHealthy()) {
                return false;
            }
        }
        return true;
    }
}
