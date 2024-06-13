package cn.clboy.clkit.websocket.handler;

import cn.clboy.clkit.common.component.security.ClkitAuthUser;
import cn.clboy.clkit.websocket.ClkitWebSocketSession;
import cn.clboy.clkit.websocket.WebsocketMessageTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ping Web socket消息处理程序
 *
 * @author clboy
 * @date 2024/06/05 17:03:31
 */
@Slf4j
@Component
public class PingWebSocketMessageHandler extends AbstractWebSocketMessageHandler<Integer> {

    public PingWebSocketMessageHandler() {
        super(WebsocketMessageTypeEnum.PING);
    }

    @Override
    public void doHandleMessage(ClkitWebSocketSession session, Integer message) {
        if (log.isDebugEnabled()) {
            ClkitAuthUser user = session.getUser();
            log.debug("收到ping消息，来自用户id: {}, 用户名: {}", user.getUserId(), user.getUsername());
        }
        session.sendPongMessage();
    }
}
