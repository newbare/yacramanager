package fr.wati.yacramanager.config.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import fr.wati.yacramanager.utils.SecurityUtils;

public class WebSocketHealthIndicator  extends AbstractHealthIndicator {

	private final Logger logger = LoggerFactory.getLogger(WebSocketHealthIndicator.class);
	private SimpMessageSendingOperations messagingTemplate;

	
	
	public WebSocketHealthIndicator(
			SimpMessageSendingOperations messagingTemplate) {
		super();
		this.messagingTemplate = messagingTemplate;
	}



	@Override
	protected void doHealthCheck(Builder builder) throws Exception {
		try {
			messagingTemplate.convertAndSendToUser(SecurityUtils.getConnectedUser().getUsername(),"/queue/errors", "ServiceCheck");
			builder.up();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			builder.down(e);
		}
	}

}
