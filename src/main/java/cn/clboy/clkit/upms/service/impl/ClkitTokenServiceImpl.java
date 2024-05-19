package cn.clboy.clkit.upms.service.impl;

import cn.clboy.clkit.common.service.CrudServiceImpl;
import cn.clboy.clkit.upms.entity.ClkitToken;
import cn.clboy.clkit.upms.repository.ClkitTokenRepository;
import cn.clboy.clkit.upms.service.ClkitTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * clKit用户服务实施
 *
 * @author clboy
 * @date 2024/05/17 16:33:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClkitTokenServiceImpl extends CrudServiceImpl<ClkitToken, Long, ClkitTokenRepository>
        implements ClkitTokenService {

    @Override
    public ClkitToken getByRefreshToken(String refreshToken) {
        return this.repository.findOne(builder -> {
            builder.equal(ClkitToken::getRefreshToken, refreshToken);
        }).orElse(null);
    }

    @Override
    public ClkitToken getByAccessToken(String accessToken) {
        return this.repository.findOne(builder -> {
            builder.equal(ClkitToken::getAccessToken, accessToken);
        }).orElse(null);
    }

    /**
     * 退出成功后删除token
     *
     * @param event 事件
     */
    @EventListener(LogoutSuccessEvent.class)
    @Transactional(rollbackFor = Exception.class)
    public void logoutSuccessEventHandle(LogoutSuccessEvent event) {
        Object accessToken = event.getAuthentication().getCredentials();
        if (accessToken == null) {
            return;
        }
        ClkitToken token = this.getByAccessToken(accessToken.toString());
        if (token != null) {
            this.removeById(token.getId());
        }
    }
}