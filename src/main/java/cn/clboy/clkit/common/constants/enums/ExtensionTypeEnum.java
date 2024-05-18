package cn.clboy.clkit.common.constants.enums;

import cn.clboy.clkit.common.component.jpa.AbstractValueLabelEnumConverter;
import cn.clboy.clkit.common.constants.DictConstant;
import cn.clboy.clkit.common.constants.DictDeclare;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 扩展类型列举
 *
 * @author clboy
 * @date 2024/05/06 09:48:53
 */
@Getter
@AllArgsConstructor
public enum ExtensionTypeEnum implements IValueLabelEnum<ExtensionTypeEnum> {
    FOLDER("0", "文件夹", false),
    ONLINE_URL("1", "在线网页", false),
    DISK_FILE("2", "磁盘文件", true),
    ;

    private final String value;
    private final String label;
    private final boolean needInstall;

    static {
        DictConstant.registerDict(new DictDeclare("extension_type", "扩展类型", ExtensionTypeEnum.class));
    }

    public static class JpaConverter extends AbstractValueLabelEnumConverter<ExtensionTypeEnum> {
    }

}
