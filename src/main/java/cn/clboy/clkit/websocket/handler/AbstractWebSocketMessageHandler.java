package cn.clboy.clkit.websocket.handler;

import cn.clboy.clkit.websocket.ClkitWebSocketSession;
import cn.clboy.clkit.websocket.WebsocketMessageTypeEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.ParameterizedType;

/**
 * 抽象websocket消息处理
 *
 * @author clboy
 * @date 2024/06/02 18:19:48
 */
public abstract class AbstractWebSocketMessageHandler<T>
        implements WebSocketMessageHandler<T>, InitializingBean {
    private final Class<T> payloadClass;
    private final WebsocketMessageTypeEnum type;

    @SuppressWarnings("all")
    public AbstractWebSocketMessageHandler(WebsocketMessageTypeEnum type) {
        this.type = type;
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.payloadClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }


    @Override
    public void handleMessage(WebSocketSession session, Object message) {
        this.doHandleMessage(ClkitWebSocketSession.wrap(session), (T) message);
    }

    protected abstract void doHandleMessage(ClkitWebSocketSession session, T message);

    public String getType() {
        return this.type.getValue();
    }

    @Override
    public Class<T> getPayloadClass() {
        return this.payloadClass;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        WebSocketMessageHandler.HOLDER.addHandler(this.getType(), this);
    }
}
