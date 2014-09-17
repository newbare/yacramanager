package fr.wati.yacramanager.config.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.codahale.metrics.health.HealthCheck;

import fr.wati.yacramanager.utils.SecurityUtils;

@Component
public class WebSocketHealthCheck extends HealthCheck {

	private Logger logger = LoggerFactory.getLogger(WebSocketHealthCheck.class);
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@Override
	protected Result check() throws Exception {
		try {
			messagingTemplate.convertAndSendToUser(SecurityUtils.getConnectedUser().getUsername(),"/queue/errors", "ServiceCheck");
			return Result.healthy();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Result.unhealthy(e);
		}
	}

}
