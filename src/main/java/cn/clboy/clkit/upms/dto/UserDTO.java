package cn.clboy.clkit.upms.dto;


import cn.clboy.clkit.common.constants.enums.RuleConstant;
import cn.clboy.clkit.common.validation.groups.Create;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户DTO
 *
 * @author clboy
 * @date 2024/05/29 09:19:34
 */
@Data
@Schema(description = "用户DTO")
public class UserDTO {

    /**
     * 用户名
     */
    @NotBlank
    @Schema(description = "用户名")
    @Pattern(regexp = RuleConstant.USERNAME_RULE)
    private String name;

    /**
     * 昵称
     */
    @NotBlank
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 真实姓名
     */
    @NotBlank
    @Schema(description = "真实姓名")
    private String realName;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @NotBlank(groups = Create.class)
    @Size(min = 6, max = 16, groups = Create.class)
    private String password;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 角色id
     */
    @Schema(description = "角色id")
    private List<Long> roleIds;
}
