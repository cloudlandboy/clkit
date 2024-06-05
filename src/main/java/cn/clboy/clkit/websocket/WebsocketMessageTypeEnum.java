package cn.clboy.clkit.websocket;

import cn.clboy.clkit.common.constants.enums.IValueLabelEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * websocket消息类型枚举
 *
 * @author clboy
 * @date 2024/06/02 18:13:00
 */
@Getter
@AllArgsConstructor
public enum WebsocketMessageTypeEnum implements IValueLabelEnum<WebsocketMessageTypeEnum> {

    PING("0", "ping"),
    PONG("1", "pong"),
    SYSTEM_NOTICE("2", "系统通知"),
    ;

    private final String value;
    private final String label;
}
