package cn.clboy.clkit.websocket;

import cn.clboy.clkit.common.component.security.ClkitAuthUser;
import cn.clboy.clkit.common.constants.ClkitConstant;
import cn.clboy.clkit.common.util.SecurityUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;


/**
 * token握手拦截器
 *
 * @author clboy
 * @date 2024/06/02 16:57:20
 */
public class TokenHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        ClkitAuthUser user = SecurityUtils.getLoginUserNonNull();
        String token = (String) req.getServletRequest().getAttribute(ClkitConstant.TOKEN_ATTRIBUTE_KEY);
        attributes.put(ClkitConstant.USER_ATTRIBUTE_KEY, user);
        attributes.put(ClkitConstant.TOKEN_ATTRIBUTE_KEY, token);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {

    }
}
