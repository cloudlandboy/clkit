package cn.clboy.clkit.common.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 属性列举
 *
 * @author clboy
 * @date 2024/06/05 09:41:53
 */
@Getter
@AllArgsConstructor
public enum PropertyEnum implements IValueLabelEnum<PropertyEnum> {

    HOME_PATH("CLKIT_HOME_PATH", "主页路径", "路由地址", "/", true),
    CLKIT_EXTENSION_MODE("CLKIT_EXTENSION_MODE", "扩展模式", "MENU/TAB", "MENU", true),
    DATA_INITIALIZED("CLKIT_DATA_INITIALIZED", "程序数据已初始化", "", "false", false);

    private final String value;
    private final String label;
    private final String desc;
    private final String initValue;
    private final boolean editable;
}
