package fr.wati.yacramanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.StompBrokerRelayRegistration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Autowired Environment env;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/yacra").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//registry.enableSimpleBroker("/topic");
		StompBrokerRelayRegistration stompBrokerRelay = registry.enableStompBrokerRelay("/queue/", "/topic/");
		stompBrokerRelay.setRelayHost(env.getProperty("stomp.broker.relay.host", "localhost"));
		stompBrokerRelay.setRelayPort(env.getProperty("stomp.broker.relay.port", Integer.class));
		stompBrokerRelay.setClientLogin(env.getProperty("stomp.broker.client.login", "guest"));
		stompBrokerRelay.setClientPasscode(env.getProperty("stomp.broker.client.passcode", "guest"));
		registry.setApplicationDestinationPrefixes("/app");
	}

}
