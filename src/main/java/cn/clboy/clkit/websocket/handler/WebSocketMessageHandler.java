package cn.clboy.clkit.websocket.handler;

import cn.clboy.clkit.common.holder.HandlerHolder;
import org.springframework.web.socket.WebSocketSession;

/**
 * websocket消息处理
 *
 * @author clboy
 * @date 2024/06/02 16:32:55
 */
public interface WebSocketMessageHandler<T> {

    HandlerHolder<String, WebSocketMessageHandler<?>> HOLDER = new HandlerHolder<>();

    Class<T> getPayloadClass();

    void handleMessage(WebSocketSession session, Object message);

}
