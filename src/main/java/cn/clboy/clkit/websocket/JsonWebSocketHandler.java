package cn.clboy.clkit.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * json格式websocket消息处理
 *
 * @author clboy
 * @date 2024/06/02 16:32:55
 */
@RequiredArgsConstructor
public class JsonWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    public static final String TYPE_KEY = "type";
    public static final String PAYLOAD_KEY = "payload";

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(message.getPayload());
        if (!jsonNode.has(TYPE_KEY)) {
            return;
        }

        String type = jsonNode.get(TYPE_KEY).asText();

        JsonNode payload = jsonNode.get(PAYLOAD_KEY);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebsocketSessionHolder.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebsocketSessionHolder.removeSession(session);
    }
}
