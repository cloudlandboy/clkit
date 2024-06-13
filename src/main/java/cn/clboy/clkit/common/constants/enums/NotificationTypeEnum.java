package cn.clboy.clkit.common.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知类型列举
 *
 * @author clboy
 * @date 2024/06/12 15:41:08
 */
@Getter
@AllArgsConstructor
public enum NotificationTypeEnum implements IValueLabelEnum<NotificationTypeEnum> {

    SUCCESS("success", "成功"),
    WARNING("warning", "警告"),
    INFO("info", "信息"),
    ERROR("error", "错误");

    private final String value;
    private final String label;
}
