package cn.clboy.clkit.common.component.security;

import cn.clboy.clkit.common.util.AppUtils;
import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.config.AppProperties;
import cn.clboy.clkit.upms.entity.ClkitToken;
import cn.clboy.clkit.upms.service.ClkitTokenService;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 认证成功处理程序
 *
 * @author clboy
 * @date 2024/05/18 22:09:36
 */
@AllArgsConstructor
public class ClkitAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final ClkitTokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                        Authentication authentication) throws IOException, ServletException {
        this.onAuthenticationSuccess(request, response, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        ClkitAuthUser user = (ClkitAuthUser) authentication.getPrincipal();
        ClkitToken clkitToken = new ClkitToken();
        clkitToken.setUserId(user.getUserId());
        clkitToken.setAccessToken(IdUtil.fastSimpleUUID());
        clkitToken.setRefreshToken(IdUtil.fastSimpleUUID());
        LocalDateTime now = LocalDateTime.now();
        AppProperties properties = AppUtils.getAppProperties();
        clkitToken.setAccessTokenExpireTime(now.plusSeconds(properties.getTokenDurationUnit()
                .toSeconds(properties.getAccessTokenDuration())));
        clkitToken.setRefreshTokenExpireTime(now.plusSeconds(properties.getTokenDurationUnit()
                .toSeconds(properties.getRefreshTokenDuration())));
        tokenService.save(clkitToken);
        //response token
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(ApiResult.ok(clkitToken)));
    }
}
