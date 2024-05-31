package cn.clboy.clkit.upms.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 密码dto
 *
 * @author clboy
 * @date 2024/05/30 17:57:53
 */
@Data
public class UserPasswordDTO {

    /**
     * 旧密码
     */
    @NotBlank
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank
    @Size(min = 6, max = 16)
    private String newPassword;

}
