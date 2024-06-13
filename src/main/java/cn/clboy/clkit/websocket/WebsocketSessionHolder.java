package cn.clboy.clkit.websocket;

import cn.clboy.clkit.websocket.message.JsonMessage;

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
    private static final Map<Long, List<ClkitWebSocketSession>> MAP = new ConcurrentHashMap<>();

    public static void addSession(ClkitWebSocketSession session) {
        List<ClkitWebSocketSession> userSessionList = MAP.computeIfAbsent(session.getUser().getUserId(),
                userId -> Collections.synchronizedList(new ArrayList<>()));
        userSessionList.add(session);
    }

    public static void removeSession(ClkitWebSocketSession session) {
        Long userId = session.getUser().getUserId();
        List<ClkitWebSocketSession> userSessionList = MAP.get(userId);
        if (userSessionList != null) {
            userSessionList.removeIf(cs -> cs.getSession() == session.getSession());
            if (userSessionList.isEmpty()) {
                MAP.remove(userId);
            }
        }

    }

    public static List<ClkitWebSocketSession> getUserSession(Long userId) {
        return MAP.getOrDefault(userId, Collections.emptyList());
    }

    public static List<ClkitWebSocketSession> getSessionList() {
        return MAP.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 广播消息
     *
     * @param message 消息
     */
    public static void broadcastMessage(JsonMessage message) {
        sendMessage(message, getSessionList());
    }

    /**
     * 发送消息
     *
     * @param userId  用户id
     * @param message 消息
     */
    public static void sendMessage(Long userId, JsonMessage message) {
        getUserSession(userId).forEach(session -> session.sendMessage(message));
    }

    /**
     * 发送消息
     *
     * @param message     消息
     * @param sessionList 会话列表
     */
    public static void sendMessage(JsonMessage message, List<ClkitWebSocketSession> sessionList) {
        sessionList.forEach(session -> session.sendMessage(message));
    }

}
