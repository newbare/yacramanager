package fr.wati.yacramanager.config.metrics;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@Configuration
public class HealthIndicatorConfiguration {

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private DataSource dataSource;
    
    @Inject
    private SimpMessageSendingOperations messagingTemplate;

    @Bean
    public HealthIndicator dbHealthIndicator() {
        return new DatabaseHealthIndicator(dataSource);
    }

    @Bean
    public HealthIndicator mailHealthIndicator() {
        return new JavaMailHealthIndicator(javaMailSender);
    }
    
    @Bean
	public WebSocketHealthIndicator webSocketHealthCheck() {
		return new WebSocketHealthIndicator(messagingTemplate);
	}
}
