package cn.clboy.clkit.config;

import cn.clboy.clkit.common.constants.ClkitConstant;
import cn.clboy.clkit.websocket.TokenHandshakeInterceptor;
import cn.clboy.clkit.websocket.handler.JsonWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * websocket配置
 *
 * @author clboy
 * @date 2024/06/02 16:59:50
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketConfigurer {

    private final ObjectMapper objectMapper;

    @Bean
    public WebSocketHandler jsonWebSocketHandler() {
        return new JsonWebSocketHandler(objectMapper);
    }

    @Bean
    public HandshakeInterceptor tokenHandshakeInterceptor() {
        return new TokenHandshakeInterceptor();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(jsonWebSocketHandler(), ClkitConstant.WEBSOCKET_ENDPOINT_PATH)
                .setAllowedOrigins("*").addInterceptors(tokenHandshakeInterceptor());
    }
}
