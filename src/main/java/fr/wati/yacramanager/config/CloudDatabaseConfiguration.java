package fr.wati.yacramanager.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import fr.wati.yacramanager.dao.JdbcCompanyInvitationRepository;

@Configuration
@Profile(Constants.SPRING_PROFILE_CLOUD)
public class CloudDatabaseConfiguration extends AbstractCloudConfig {

    private final Logger log = LoggerFactory.getLogger(CloudDatabaseConfiguration.class);

    @Bean
    public DataSource dataSource() {
        log.info("Configuring JDBC datasource from a cloud provider");
        return connectionFactory().dataSource();
    }
    
    @Bean
	public JdbcCompanyInvitationRepository companyInvitationRepository(){
		return new JdbcCompanyInvitationRepository(dataSource());
	}
}
