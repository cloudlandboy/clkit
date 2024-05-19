package cn.clboy.clkit.common.component.security;

import cn.clboy.clkit.common.constants.ClkitConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

/**
 * 权限检查器
 *
 * @author clboy
 * @date 2024/05/18 19:15:48
 */
@Component("authChecker")
public class AuthChecker {

    public boolean hasPermission(String permission) {
        if (permission == null) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream().anyMatch(ga -> {
            if (ClkitConstant.ADMIN_ROLE_CODE.equals(ga.getAuthority())) {
                return true;
            }
            return PatternMatchUtils.simpleMatch(permission, ga.getAuthority());
        });
    }
}
