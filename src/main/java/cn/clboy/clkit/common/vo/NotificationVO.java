package cn.clboy.clkit.common.vo;

import cn.clboy.clkit.common.constants.enums.NotificationTypeEnum;
import lombok.Data;

/**
 * 系统通知数据
 *
 * @author clboy
 * @date 2024/06/12 15:29:41
 */
@Data
public class NotificationVO {

    private NotificationTypeEnum type;
    private String title;
    private String message;
}
