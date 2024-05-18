package cn.clboy.clkit.common.constants.enums;

import cn.clboy.clkit.common.component.jpa.AbstractValueLabelEnumConverter;
import cn.clboy.clkit.common.constants.DictConstant;
import cn.clboy.clkit.common.constants.DictDeclare;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 待做事项状态枚举
 *
 * @author clboy
 * @date 2024/05/10 14:50:36
 */
@Getter
@AllArgsConstructor
public enum TodoStatusEnum implements IValueLabelEnum<TodoStatusEnum> {

    /**
     * 未完成
     */
    UNDONE("0", "未完成"),

    /**
     * 已完成
     */
    DONE("1", "已完成"),

    /**
     * 已过期
     */
    EXPIRED("2", "已过期"),
    ;

    private final String value;
    private final String label;

    static {
        DictConstant.registerDict(new DictDeclare("todo_status", "待办状态", TodoStatusEnum.class));
    }

    public static class JapConverter extends AbstractValueLabelEnumConverter<TodoStatusEnum> {
    }
}
