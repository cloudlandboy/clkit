package cn.clboy.clkit.common.component.security;

import cn.clboy.clkit.upms.entity.ClkitUser;
import cn.clboy.clkit.upms.entity.Permission;
import cn.clboy.clkit.upms.entity.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    /**
     * 权限
     */
    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static ClkitAuthUser withClkitUser(ClkitUser user) {
        ClkitAuthUser authUser = new ClkitAuthUser();
        authUser.setUserId(user.getId());
        authUser.setUsername(user.getName());
        authUser.setPassword(user.getPassword());
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(user.getRole())) {
            for (Role role : user.getRole()) {
                authorities.add(new SimpleGrantedAuthority(role.getCode()));
                if (CollectionUtils.isEmpty(role.getPermission())) {
                    continue;
                }
                for (Permission permission : role.getPermission()) {
                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
                }
            }
        }
        authUser.setAuthorities(authorities);
        return authUser;
    }
}
