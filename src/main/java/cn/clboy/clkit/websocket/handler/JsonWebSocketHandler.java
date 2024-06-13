package cn.clboy.clkit.websocket.handler;

import cn.clboy.clkit.websocket.ClkitWebSocketSession;
import cn.clboy.clkit.websocket.WebsocketSessionHolder;
import cn.clboy.clkit.websocket.message.JsonMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JsonWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    public JsonWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            JsonNode jsonNode = objectMapper.readTree(message.getPayload());
            if (!jsonNode.has(JsonMessage.TYPE_KEY)) {
                return;
            }

            String type = jsonNode.get(JsonMessage.TYPE_KEY).asText();
            JsonNode payload = jsonNode.get(JsonMessage.PAYLOAD_KEY);
            WebSocketMessageHandler<?> handler = WebSocketMessageHandler.HOLDER.getHandler(type);
            if (handler == null) {
                return;
            }

            Object content = objectMapper.treeToValue(payload, handler.getPayloadClass());
            handler.handleMessage(session, content);
        } catch (Exception ex) {
            log.error("处理websocket消息失败", ex);
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        ClkitWebSocketSession wrapSession = ClkitWebSocketSession.wrap(session);
        WebsocketSessionHolder.addSession(wrapSession);
        wrapSession.sendPongMessage();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebsocketSessionHolder.removeSession(ClkitWebSocketSession.wrap(session));
    }

}
