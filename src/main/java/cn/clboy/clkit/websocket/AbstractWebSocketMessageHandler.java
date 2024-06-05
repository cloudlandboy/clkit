package cn.clboy.clkit.websocket;

import org.springframework.beans.factory.InitializingBean;

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
