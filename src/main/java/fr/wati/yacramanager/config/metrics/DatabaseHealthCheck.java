package fr.wati.yacramanager.config.metrics;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.codahale.metrics.health.HealthCheck;

@Component
public class DatabaseHealthCheck extends HealthCheck implements InitializingBean {
	@Inject
	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;


	private static Map<String, String> queries = new HashMap<String, String>();

	static {
		queries.put("HSQL Database Engine",
				"SELECT COUNT(*) FROM INFORMATION_SCHEMA.SYSTEM_USERS");
		queries.put("Oracle", "SELECT 'Hello' from DUAL");
		queries.put("Apache Derby", "SELECT 1 FROM SYSIBM.SYSDUMMY1");
		queries.put("MySQL", "SELECT 1");
		queries.put("PostgreSQL", "SELECT 1");
		queries.put("Microsoft SQL Server", "SELECT 1");
	}

	private static String DEFAULT_QUERY = "SELECT 1";

	private String query = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codahale.metrics.health.HealthCheck#check()
	 */
	@Override
	protected Result check() throws Exception {
		String product = getProduct();
		String query = detectQuery(product);
		if (StringUtils.hasText(query)) {
			try {
				this.jdbcTemplate.queryForObject(query, Object.class);
				return Result.healthy();
			} catch (Exception ex) {
				return Result.unhealthy(ex);
			}
		}

		return Result.healthy();
	}

	private String getProduct() {
		return this.jdbcTemplate.execute(new ConnectionCallback<String>() {
			@Override
			public String doInConnection(Connection connection)
					throws SQLException, DataAccessException {

				return connection.getMetaData().getDatabaseProductName();
			}
		});
	}

	protected String detectQuery(String product) {
		String query = this.query;
		if (!StringUtils.hasText(query)) {
			query = queries.get(product);
		}
		if (!StringUtils.hasText(query)) {
			query = DEFAULT_QUERY;
		}
		return query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
