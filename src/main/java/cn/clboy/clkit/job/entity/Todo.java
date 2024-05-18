package cn.clboy.clkit.job.entity;

import cn.clboy.clkit.common.constants.enums.JobReminderTimeEnum;
import cn.clboy.clkit.common.constants.enums.JobRepeatModeEnum;
import cn.clboy.clkit.common.constants.enums.TodoStatusEnum;
import cn.clboy.clkit.common.entity.BaseEntity;
import cn.clboy.clkit.common.entity.IVersionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 待做事项
 *
 * @author clboy
 * @date 2024/05/10 09:47:54
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "idx_execution_time", columnList = "deadlineTime DESC"),
        @Index(name = "idx_reminder_time", columnList = "reminderTime"),
})
@EqualsAndHashCode(callSuper = true)
public class Todo extends BaseEntity implements IVersionEntity {

    /**
     * 家族id
     */
    @Comment("家族id")
    @Column(nullable = false, updatable = false)
    private String familyId;

    /**
     * 名称
     */
    @Comment("名称")
    @Column(nullable = false)
    @NotBlank(message = "invalid name")
    private String name;

    /**
     * 备注
     */
    @Comment("备注")
    private String remark;

    /**
     * 重复
     */
    @Comment("重复")
    @Column(nullable = false)
    @Convert(converter = JobRepeatModeEnum.JapConverter.class)
    @NotNull(message = "invalid repeat")
    private JobRepeatModeEnum repeat;

    /**
     * 提醒
     */
    @Comment("提醒")
    @Column(nullable = false)
    @NotNull(message = "invalid reminder")
    @Convert(converter = JobReminderTimeEnum.JapConverter.class)
    private JobReminderTimeEnum reminder;

    /**
     * 截止时间
     */
    @Comment("截止时间")
    @Column(nullable = false)
    @NotNull(message = "invalid deadlineTime")
    private LocalDateTime deadlineTime;

    /**
     * 提醒时间
     */
    @Comment("提醒时间")
    private LocalDateTime reminderTime;

    /**
     * 重复停止时间
     */
    @Comment("重复停止时间")
    private LocalDateTime repeatStopTime;

    /**
     * 状态
     */
    @Comment("状态")
    @Column(nullable = false)
    @Convert(converter = TodoStatusEnum.JapConverter.class)
    private TodoStatusEnum status;

    /**
     * 版本
     */
    @Version
    @Comment("版本")
    @Column(nullable = false)
    private Integer version;

    /**
     * 设定截止日期时间，秒固定为0
     *
     * @param deadlineTime 截止期限时间
     */
    public void setDeadlineTime(@NotNull(message = "invalid deadlineTime") LocalDateTime deadlineTime) {
        this.deadlineTime = deadlineTime.withSecond(0);
    }
}
