package cn.clboy.clkit.common.constants.enums;

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
    FOLDER("文件夹", "0", false),
    ONLINE_URL("在线网页", "1", false),
    DISK_FILE("磁盘文件", "2", true),
    ;

    private final String label;
    private final String value;
    private final boolean needInstall;
}
