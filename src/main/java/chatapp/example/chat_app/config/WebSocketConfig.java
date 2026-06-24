package chatapp.example.chat_app.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // This is the URL the frontend will use to connect to the server
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Messages whose destination starts with "/app" are routed to message-handling methods in your Controller
        registry.setApplicationDestinationPrefixes("/app");
        // Messages whose destination starts with "/topic" are broadcasted to all connected clients
        registry.enableSimpleBroker("/topic");
    }
}
