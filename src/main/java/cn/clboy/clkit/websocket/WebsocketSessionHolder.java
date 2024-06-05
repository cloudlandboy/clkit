package cn.clboy.clkit.websocket;

import cn.clboy.clkit.common.component.security.ClkitAuthUser;
import cn.clboy.clkit.common.constants.ClkitConstant;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * websocket session容器
 *
 * @author clboy
 * @date 2024/06/02 17:47:17
 */
public class WebsocketSessionHolder {

    private WebsocketSessionHolder() {

    }

    /**
     * 用户id:sessionList
     */
    private static final Map<Long, List<WebSocketSession>> MAP = new ConcurrentHashMap<>();

    public static void addSession(WebSocketSession session) {
        ClkitAuthUser user = (ClkitAuthUser) session.getAttributes().get(ClkitConstant.USER_ATTRIBUTE_KEY);
        List<WebSocketSession> userSessionList = MAP.computeIfAbsent(user.getUserId(),
                userId -> Collections.synchronizedList(new ArrayList<>()));
        userSessionList.add(session);
    }

    public static void removeSession(WebSocketSession session) {
        ClkitAuthUser user = (ClkitAuthUser) session.getAttributes().get(ClkitConstant.USER_ATTRIBUTE_KEY);
        List<WebSocketSession> userSessionList = MAP.get(user.getUserId());
        if (userSessionList != null) {
            userSessionList.remove(session);
            if (userSessionList.isEmpty()) {
                MAP.remove(user.getUserId());
            }
        }

    }

    public static List<WebSocketSession> getUserSession(Long userId) {
        return MAP.getOrDefault(userId, Collections.emptyList());
    }

    public static List<WebSocketSession> getSessionList() {
        return MAP.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
