package cn.clboy.clkit.common.component.security;

import cn.clboy.clkit.upms.entity.ClkitUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * clKit授权用户
 *
 * @author clboy
 * @date 2024/05/17 16:39:44
 */
@Data
public class ClkitAuthUser implements UserDetails {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public static ClkitAuthUser withClkitUser(ClkitUser user) {
        ClkitAuthUser authUser = new ClkitAuthUser();
        authUser.setUserId(user.getId());
        authUser.setUsername(user.getName());
        authUser.setPassword(user.getPassword());
        return authUser;
    }
}
