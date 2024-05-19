package cn.clboy.clkit.upms.service;

import cn.clboy.clkit.common.service.CrudService;
import cn.clboy.clkit.upms.entity.ClkitToken;


/**
 * clKit令牌服务
 *
 * @author clboy
 * @date 2024/05/18 22:13:32
 */
public interface ClkitTokenService extends CrudService<ClkitToken, Long> {

    /**
     * 获取通过刷新令牌
     *
     * @param refreshToken 刷新令牌
     */
    ClkitToken getByRefreshToken(String refreshToken);

    /**
     * 获取通过接入令牌
     *
     * @param accessToken 访问令牌
     */
    ClkitToken getByAccessToken(String accessToken);
}
