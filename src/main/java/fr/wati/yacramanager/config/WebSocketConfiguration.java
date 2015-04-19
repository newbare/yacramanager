package fr.wati.yacramanager.config;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.StompBrokerRelayRegistration;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer implements EnvironmentAware {

	private RelaxedPropertyResolver propertyResolver;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.EnvironmentAware#setEnvironment(org.
	 * springframework.core.env.Environment)
	 */
	@Override
	public void setEnvironment(Environment environment) {
		this.propertyResolver = new RelaxedPropertyResolver(environment,
				"spring.websocket.");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/yacra").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// registry.enableSimpleBroker("/topic");
		StompBrokerRelayRegistration stompBrokerRelay = registry
				.enableStompBrokerRelay("/queue/", "/topic/");
		stompBrokerRelay.setRelayHost(propertyResolver.getProperty(
				"stomp.broker.relay.host", "localhost"));
		stompBrokerRelay.setRelayPort(propertyResolver.getProperty(
				"stomp.broker.relay.port", Integer.class, 61613));
		stompBrokerRelay.setClientLogin(propertyResolver.getProperty(
				"stomp.broker.client.login", "guest"));
		stompBrokerRelay.setClientPasscode(propertyResolver.getProperty(
				"stomp.broker.client.passcode", "guest"));
		registry.setApplicationDestinationPrefixes("/app");
	}

}
