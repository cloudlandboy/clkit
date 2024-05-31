package cn.clboy.clkit.upms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 角色DTO
 *
 * @author clboy
 * @date 2024/05/28 09:53:27
 */
@Data
@Schema(description = "角色DTO")
public class RoleDTO {

    /**
     * 名称
     */
    @NotBlank
    @Schema(description = "名称")
    private String name;

    /**
     * 编码
     */
    @NotBlank
    @Schema(description = "编码")
    private String code;

    /**
     * 权限id
     */
    @Schema(description = "权限id")
    private List<Long> permissionIds;
}
