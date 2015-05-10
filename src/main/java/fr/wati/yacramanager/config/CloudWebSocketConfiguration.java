//package fr.wati.yacramanager.config;
//
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.cloud.config.java.AbstractCloudConfig;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.env.Environment;
//
//@Configuration
//@Profile({Constants.SPRING_PROFILE_CLOUD})
//public class CloudWebSocketConfiguration extends AbstractCloudConfig implements EnvironmentAware {
//
//	private Environment environment;
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.springframework.context.EnvironmentAware#setEnvironment(org.
//	 * springframework.core.env.Environment)
//	 */
//	@Override
//	public void setEnvironment(Environment environment) {
//		this.environment = environment;
//	}
//
//	@Bean
//	public ConnectionFactory rabbitConnectionFactory(){
//		return connectionFactory().rabbitConnectionFactory();
//	}
//
//}
