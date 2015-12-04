package grails.plugin.springwebsocket

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.session.ExpiringSession
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry

@Configuration
@EnableWebSocketMessageBroker
class DefaultWebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {

	@Override
	protected void configureStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/queue/", "/topic/", "/exchange/");
		//registry.enableStompBrokerRelay("/queue/", "/topic/", "/exchange/");
		registry.setApplicationDestinationPrefixes("/app");
	}
	@Bean
	GrailsSimpAnnotationMethodMessageHandler grailsSimpAnnotationMethodMessageHandler(
			MessageChannel clientInboundChannel,
			MessageChannel clientOutboundChannel,
			SimpMessagingTemplate brokerMessagingTemplate
	) {
		def handler = new GrailsSimpAnnotationMethodMessageHandler(clientInboundChannel, clientOutboundChannel, brokerMessagingTemplate)
		handler.destinationPrefixes = ["/app"]
		return handler
	}
	
}
