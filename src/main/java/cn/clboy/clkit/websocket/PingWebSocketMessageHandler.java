package cn.clboy.clkit.websocket;

import org.springframework.web.socket.WebSocketSession;

public class PingWebSocketMessageHandler extends AbstractWebSocketMessageHandler<Integer> {

    public PingWebSocketMessageHandler() {
        super(WebsocketMessageTypeEnum.PING);
    }

    @Override
    public void handleMessage(WebSocketSession session, Integer message) {

    }
}
