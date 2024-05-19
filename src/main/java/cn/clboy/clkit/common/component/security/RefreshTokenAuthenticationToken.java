package cn.clboy.clkit.common.component.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

/**
 * 刷新令牌验证令牌
 *
 * @author clboy
 * @date 2024/05/19 00:14:24
 */
public class RefreshTokenAuthenticationToken extends AbstractAuthenticationToken {

    private final String refreshToken;

    public RefreshTokenAuthenticationToken(String refreshToken) {
        super(Collections.emptyList());
        this.refreshToken = refreshToken;
    }

    @Override
    public Object getCredentials() {
        return refreshToken;
    }

    @Override
    public Object getPrincipal() {
        return refreshToken;
    }
}
