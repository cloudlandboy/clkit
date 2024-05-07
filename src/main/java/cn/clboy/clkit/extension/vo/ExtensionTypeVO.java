package cn.clboy.clkit.extension.vo;

import cn.clboy.clkit.common.vo.ValueLabelVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 扩展类型数据
 *
 * @author clboy
 * @date 2024/05/06 10:23:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExtensionTypeVO extends ValueLabelVO {

    private Boolean needInstall;
}
