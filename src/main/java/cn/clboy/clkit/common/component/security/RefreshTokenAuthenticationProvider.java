package cn.clboy.clkit.common.component.security;

import cn.clboy.clkit.upms.entity.ClkitToken;
import cn.clboy.clkit.upms.service.ClkitTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

/**
 * 刷新令牌身份验证提供商
 *
 * @author clboy
 * @date 2024/05/19 00:00:12
 */
@AllArgsConstructor
public class RefreshTokenAuthenticationProvider implements AuthenticationProvider {

    private final ClkitTokenService tokenService;
    private final ClkitUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String refreshToken = (String) authentication.getPrincipal();
        ClkitToken token = tokenService.getByRefreshToken(refreshToken);
        if (token == null) {
            throw new BadCredentialsException("无效的令牌");
        }
        if (LocalDateTime.now().isAfter(token.getRefreshTokenExpireTime())) {
            tokenService.removeById(token.getId());
            throw new CredentialsExpiredException("无效的令牌");
        }
        UserDetails userDetails = userDetailsService.loadUserById(token.getUserId());
        //删除旧令牌，稍后重新生成  ClkitAuthenticationSuccessHandler
        tokenService.removeById(token.getId());
        return new UsernamePasswordAuthenticationToken(userDetails, refreshToken, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
