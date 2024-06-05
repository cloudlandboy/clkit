package cn.clboy.clkit.common.component.security;

import cn.clboy.clkit.common.constants.ClkitConstant;
import cn.clboy.clkit.upms.entity.ClkitToken;
import cn.clboy.clkit.upms.service.ClkitTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 承载令牌过滤器
 *
 * @author clboy
 * @date 2024/05/19 00:47:40
 */
@AllArgsConstructor
public class BearerTokenFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";

    private final ClkitTokenService tokenService;
    private final ClkitUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(accessToken)) {
            if (StringUtils.startsWithIgnoreCase(accessToken, BEARER_PREFIX)) {
                accessToken = accessToken.substring(BEARER_PREFIX.length());
            }
        } else {
            accessToken = request.getParameter("accessToken");
        }

        if (!StringUtils.hasText(accessToken)) {
            chain.doFilter(request, response);
            return;
        }
        ClkitToken token = tokenService.getByAccessToken(accessToken);
        if (token == null || LocalDateTime.now().isAfter(token.getAccessTokenExpireTime())) {
            chain.doFilter(request, response);
            return;
        }

        request.setAttribute(ClkitConstant.TOKEN_ATTRIBUTE_KEY, accessToken);
        ClkitAuthUser user = userDetailsService.loadUserById(token.getUserId());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, accessToken, user.getAuthorities()));
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }
}
