package cn.clboy.clkit.common.constants;

import cn.clboy.clkit.common.constants.enums.IValueLabelEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 字典声明
 *
 * @author clboy
 * @date 2024/05/15 15:26:45
 */
@Data
@AllArgsConstructor
public class DictDeclare {
    private final String type;
    private final String desc;
    private final Class<? extends IValueLabelEnum> enumClass;
}
