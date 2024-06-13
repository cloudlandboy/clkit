package cn.clboy.clkit.websocket.message;

import cn.clboy.clkit.websocket.WebsocketMessageTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

/**
 * json Web socket消息
 *
 * @author clboy
 * @date 2024/06/05 16:25:21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonMessage<T> {

    public static final JsonMessage PONE = new JsonMessage(WebsocketMessageTypeEnum.PONG.getValue(), 1);

    public static final String TYPE_KEY = "type";
    public static final String PAYLOAD_KEY = "payload";

    @Getter
    private final String type;

    @Getter
    private final T payload;

    public JsonMessage(String type, T data) {
        this.type = type;
        this.payload = data;
    }


}
