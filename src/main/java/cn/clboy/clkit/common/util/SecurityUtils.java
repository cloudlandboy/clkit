package cn.clboy.clkit.common.util;

import cn.clboy.clkit.common.component.security.ClkitAuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

/**
 * 安全实用程序
 *
 * @author clboy
 * @date 2024/05/19 09:03:39
 */
public class SecurityUtils {

    /**
     * 获取登录用户
     */
    public static ClkitAuthUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return toAuthUser(authentication);
    }

    /**
     * 获取登录用户非null
     */
    public static ClkitAuthUser getLoginUserNonNull() {
        ClkitAuthUser loginUser = getLoginUser();
        Assert.notNull(loginUser, "no login user found");
        return loginUser;
    }

    /**
     * 转为登录用户
     *
     * @param authentication 认证
     */
    public static ClkitAuthUser toAuthUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        return (principal instanceof ClkitAuthUser) ? (ClkitAuthUser) principal : null;
    }
}
