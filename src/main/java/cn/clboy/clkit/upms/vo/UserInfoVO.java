package cn.clboy.clkit.upms.vo;

import cn.clboy.clkit.upms.entity.ClkitUser;
import cn.clboy.clkit.upms.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息数据
 *
 * @author clboy
 * @date 2024/05/19 08:54:12
 */
@Data
@Schema(description = "用户信息")
public class UserInfoVO {

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String name;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    private String realName;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 角色列表
     */
    private List<String> roleNameList;

    /**
     * 权限列表
     */
    @Schema(description = "权限列表")
    private List<String> permissionList;


    public static UserInfoVO with(ClkitUser user, Collection<? extends GrantedAuthority> authorities) {
        UserInfoVO infoVO = new UserInfoVO();
        infoVO.setName(user.getName());
        infoVO.setNickname(user.getNickname());
        infoVO.setRealName(user.getRealName());//帐号密码
        infoVO.setEmail(user.getEmail());
        infoVO.setRoleNameList(user.getRole().stream().map(Role::getName).collect(Collectors.toList()));
        infoVO.setPermissionList(authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return infoVO;
    }
}
