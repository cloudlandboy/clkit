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
 * 任务重复模式枚举
 *
 * @author clboy
 * @date 2024/05/10 10:00:02
 */
@Getter
@AllArgsConstructor
public enum JobRepeatModeEnum implements IValueLabelEnum<JobRepeatModeEnum> {

    NOT("0", "不重复", LocalDateTimeCalculator.EMPTY),
    YEAR("1", "每年", new TimeUnitLocalDateTimeCalculator(1, ChronoUnit.YEARS)),
    MONTH("2", "每月", new TimeUnitLocalDateTimeCalculator(1, ChronoUnit.MONTHS)),
    WEEK("3", "每周", new TimeUnitLocalDateTimeCalculator(1, ChronoUnit.WEEKS)),
    DAY("4", "每天", new TimeUnitLocalDateTimeCalculator(1, ChronoUnit.DAYS)),
    ;
    private final String value;
    private final String label;
    private final LocalDateTimeCalculator calculator;

    static {
        DictConstant.registerDict(new DictDeclare("job_repeat_mode", "任务重复模式", JobRepeatModeEnum.class));
    }

    public static class JapConverter extends AbstractValueLabelEnumConverter<JobRepeatModeEnum> {
    }
}
