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
    DATA_INITIALIZED("CLKIT_DATA_INITIALIZED", "程序数据已初始化", "", "false", false),
    DEEPLX_HOST("DEEPLX_HOST", "DeepLX地址", "https://deeplx.owo.network",
            "http://127.0.0.1:1188", true),
    DEEPLX_TOKEN("DEEPLX_TOKEN", "DeepLX密钥", "https://deeplx.owo.network",
            "", true),
    ;


    private final String value;
    private final String label;
    private final String desc;
    private final String initValue;
    private final boolean editable;
}
