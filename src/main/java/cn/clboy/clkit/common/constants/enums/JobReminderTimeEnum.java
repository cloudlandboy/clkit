package cn.clboy.clkit.common.constants.enums;

import cn.clboy.clkit.common.component.jpa.AbstractValueLabelEnumConverter;
import cn.clboy.clkit.common.constants.DictConstant;
import cn.clboy.clkit.common.constants.DictDeclare;
import cn.clboy.clkit.common.time.LocalDateTimeCalculator;
import cn.clboy.clkit.common.time.TimeUnitLocalDateTimeCalculator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.temporal.ChronoUnit;

/**
 * 任务提醒时间
 *
 * @author clboy
 * @date 2024/05/10 14:27:32
 */
@Getter
@AllArgsConstructor
public enum JobReminderTimeEnum implements IValueLabelEnum<JobReminderTimeEnum> {

    NOT("0", "不提醒", LocalDateTimeCalculator.EMPTY),
    PUNCTUAL("1", "正点提醒", LocalDateTimeCalculator.IDENTITY),
    MINUTE_5("2", "提前5分钟", new TimeUnitLocalDateTimeCalculator(-2, ChronoUnit.MINUTES)),
    MINUTE_15("3", "提前15分钟", new TimeUnitLocalDateTimeCalculator(-15, ChronoUnit.MINUTES)),
    MINUTE_30("4", "提前30分钟", new TimeUnitLocalDateTimeCalculator(-30, ChronoUnit.MINUTES)),
    HOUR_1("5", "提前1小时", new TimeUnitLocalDateTimeCalculator(-1, ChronoUnit.HOURS)),
    DAY_1("6", "提前1天", new TimeUnitLocalDateTimeCalculator(-1, ChronoUnit.DAYS)),
    DAY_3("7", "提前3天", new TimeUnitLocalDateTimeCalculator(-3, ChronoUnit.DAYS)),
    DAY_7("8", "提前7天", new TimeUnitLocalDateTimeCalculator(-7, ChronoUnit.DAYS)),
    ;

    private final String value;
    private final String label;
    private final LocalDateTimeCalculator calculator;

    static {
        DictConstant.registerDict(new DictDeclare("job_reminder", "任务提醒时间", JobReminderTimeEnum.class));
    }

    public static class JapConverter extends AbstractValueLabelEnumConverter<JobReminderTimeEnum> {
    }


}
