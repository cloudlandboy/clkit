package cn.clboy.clkit.websocket;

import cn.clboy.clkit.common.component.security.ClkitAuthUser;
import cn.clboy.clkit.common.constants.ClkitConstant;
import cn.clboy.clkit.common.holder.ObjectMapperHolder;
import cn.clboy.clkit.websocket.message.JsonMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * clKit Web socket会话
 *
 * @author clboy
 * @date 2024/06/05 17:18:57
 */
@RequiredArgsConstructor
public class ClkitWebSocketSession {

    @Getter
    private final WebSocketSession session;

    /**
     * 获取用户
     */
    public ClkitAuthUser getUser() {
        return (ClkitAuthUser) session.getAttributes().get(ClkitConstant.USER_ATTRIBUTE_KEY);
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    @SneakyThrows
    public void sendMessage(JsonMessage message) {
        TextMessage textMessage = new TextMessage(ObjectMapperHolder.getObjectMapper().writeValueAsString(message));
        session.sendMessage(textMessage);
    }

    /**
     * 发送pong消息
     */
    public void sendPongMessage() {
        this.sendMessage(JsonMessage.PONE);
    }

    /**
     * 包装
     *
     * @param session session
     */
    public static ClkitWebSocketSession wrap(WebSocketSession session) {
        return new ClkitWebSocketSession(session);
    }

}
