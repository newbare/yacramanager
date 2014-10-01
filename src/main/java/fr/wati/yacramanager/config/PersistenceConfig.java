package fr.wati.yacramanager.config;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate3.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="fr.wati.yacramanager.dao")
@EnableJpaAuditing
public class PersistenceConfig 
{
	private Logger log=LoggerFactory.getLogger(PersistenceConfig.class);
	@Autowired
	private Environment env;
	
	@Bean
	public PlatformTransactionManager transactionManager()
	{
		EntityManagerFactory factory = entityManagerFactory().getObject();
		return new JpaTransactionManager(factory);
	}
	@Bean
	@Profile(value={Constants.SPRING_PROFILE_DEVELOPMENT,Constants.SPRING_PROFILE_PRODUCTION})
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
	{
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(Boolean.parseBoolean(env.getProperty("jpa.generateDdl")));
		vendorAdapter.setShowSql(Boolean.parseBoolean(env.getProperty("hibernate.show_sql")));
		vendorAdapter.setDatabasePlatform(env.getProperty("hibernate.dialect"));
		factory.setDataSource(dataSource());
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("fr.wati.yacramanager.beans");
		factory.getJpaPropertyMap().put("jadira.usertype.autoRegisterUserTypes", "true");
		factory.getJpaPropertyMap().put("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
		factory.getJpaPropertyMap().put("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
		factory.getJpaPropertyMap().put("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
		factory.getJpaPropertyMap().put("hibernate.cache.region.factory_class", env.getProperty("hibernate.cache.region.factory_class"));
		
		//factory.getJpaPropertyMap().put("hibernate.ejb.naming_strategy",CustomNamingStrategy.class.getName());
//		Properties jpaProperties = new Properties();
//		jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//		factory.setJpaProperties(jpaProperties);
		factory.afterPropertiesSet();
		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return factory;
	}
	

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator()
	{
		return new HibernateExceptionTranslator();
	}
	@Bean
	@Profile(value={Constants.SPRING_PROFILE_DEVELOPMENT,Constants.SPRING_PROFILE_PRODUCTION})
	public DataSource dataSource()
	{
		log.debug("Configuring Datasource");
        if (env.getProperty("dataSource.url") == null && env.getProperty("dataSource.databaseName") == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                    "cannot start.");

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(env.getProperty("dataSource.dataSourceClassName"));
        if (env.getProperty("dataSource.url") == null || "".equals(env.getProperty("dataSource.url"))) {
            config.addDataSourceProperty("databaseName", env.getProperty("dataSource.databaseName"));
            config.addDataSourceProperty("serverName", env.getProperty("dataSource.serverName"));
        } else {
            config.addDataSourceProperty("url", env.getProperty("dataSource.url"));
        }
        config.addDataSourceProperty("user", env.getProperty("dataSource.username"));
        config.addDataSourceProperty("password", env.getProperty("dataSource.password"));

        //MySQL optimizations, see https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        if ("com.mysql.jdbc.jdbc2.optional.MysqlDataSource".equals(env.getProperty("dataSource.dataSourceClassName"))) {
            config.addDataSourceProperty("cachePrepStmts", env.getProperty("dataSource.cachePrepStmts", "true"));
            config.addDataSourceProperty("prepStmtCacheSize", env.getProperty("dataSource.prepStmtCacheSize", "250"));
            config.addDataSourceProperty("prepStmtCacheSqlLimit", env.getProperty("dataSource.prepStmtCacheSqlLimit", "2048"));
            config.addDataSourceProperty("useServerPrepStmts", env.getProperty("dataSource.useServerPrepStmts", "true"));
        }
        return new HikariDataSource(config);
		
		
//		HikariConfig config = new HikariConfig("database-yacra.properties");
//		HikariDataSource dataSource = new HikariDataSource(config);
//		BasicDataSource dataSource = new BasicDataSource();
//		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
//		dataSource.setUrl(env.getProperty("jdbc.url"));
//		dataSource.setUsername(env.getProperty("jdbc.username"));
//		dataSource.setPassword(env.getProperty("jdbc.password"));
//		return dataSource;
	}
	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) 
	{
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.setContinueOnError(true);
		databasePopulator.addScript(new ClassPathResource("sql/spring-security-persistent-login.sql"));
		databasePopulator.addScript(new ClassPathResource("sql/roles-initialization.sql"));
		dataSourceInitializer.setDatabasePopulator(databasePopulator);
		dataSourceInitializer.setEnabled(env.getProperty("init-db",Boolean.class,false));
		return dataSourceInitializer;
	}	
}