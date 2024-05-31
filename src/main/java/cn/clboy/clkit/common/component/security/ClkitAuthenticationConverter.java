package cn.clboy.clkit.common.component.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * clKit身份验证转换器
 *
 * @author clboy
 * @date 2024/05/18 23:16:53
 */
public class ClkitAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter("grant_type");
        if ("password".equals(grantType)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            return new UsernamePasswordAuthenticationToken(username, password);
        }

        if ("refresh_token".equals(grantType)) {
            String refreshToken = request.getParameter("refreshToken");
            if (StringUtils.hasText(refreshToken)) {
                return new RefreshTokenAuthenticationToken(refreshToken);
            }
        }

        return null;
    }
}
