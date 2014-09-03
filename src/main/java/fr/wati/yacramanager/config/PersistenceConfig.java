package fr.wati.yacramanager.config;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="fr.wati.yacramanager.dao")
@EnableJpaAuditing
public class PersistenceConfig 
{
	@Autowired
	private Environment env;
	
	@Bean
	public PlatformTransactionManager transactionManager()
	{
		EntityManagerFactory factory = entityManagerFactory().getObject();
		return new JpaTransactionManager(factory);
	}
	@Bean
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
//		Properties jpaProperties = new Properties();
//		jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//		factory.setJpaProperties(jpaProperties);
		factory.afterPropertiesSet();
		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return factory;
	}
	
//	@Bean
//	public HibernateTemplate hibernateTemplate(){
//		HibernateTemplate hibernateTemplate=new HibernateTemplate(sessionFactory());
//		hibernateTemplate.setSessionFactory(sessionFactory());
//		return hibernateTemplate;
//	}
//	
//	/**
//	 * @return
//	 */
//	@Bean
//	public SessionFactory sessionFactory() {
//		AnnotationSessionFactoryBean annotationSessionFactoryBean=new AnnotationSessionFactoryBean();
//		annotationSessionFactoryBean.setDataSource(dataSource());
//		annotationSessionFactoryBean.setPackagesToScan(new String[]{"fr.wati.yacramanager"});
//		Properties hibernateProperties=new Properties();
//		hibernateProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
//		hibernateProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
//		hibernateProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//		annotationSessionFactoryBean.setHibernateProperties(hibernateProperties);
//		return annotationSessionFactoryBean.getObject();
//	}
	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator()
	{
		return new HibernateExceptionTranslator();
	}
	@Bean
	public DataSource dataSource()
	{
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		return dataSource;
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
		dataSourceInitializer.setEnabled(env.getProperty("init-db",Boolean.class));
		return dataSourceInitializer;
	}	
}