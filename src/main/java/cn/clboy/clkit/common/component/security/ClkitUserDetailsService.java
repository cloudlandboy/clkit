package cn.clboy.clkit.common.component.security;

import cn.clboy.clkit.upms.service.ClkitUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * clKit用户详细信息服务
 *
 * @author clboy
 * @date 2024/05/17 16:28:47
 */
@AllArgsConstructor
public class ClkitUserDetailsService implements UserDetailsService {

    private final ClkitUserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getByName(username).map(ClkitAuthUser::withClkitUser)
                .orElseThrow(() -> new UsernameNotFoundException("用户名不存在"));
    }

    /**
     * 按ID加载用户
     *
     * @param id ID
     */
    @Transactional(readOnly = true)
    public ClkitAuthUser loadUserById(Long id) {
        return Optional.ofNullable(userService.getById(id)).map(ClkitAuthUser::withClkitUser)
                .orElseThrow(() -> new UsernameNotFoundException("用户名不存在"));
    }
}
