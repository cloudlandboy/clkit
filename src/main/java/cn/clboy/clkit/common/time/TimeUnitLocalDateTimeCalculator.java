package cn.clboy.clkit.common.time;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * 时间单位本地日期时间计算器
 *
 * @author clboy
 * @date 2024/05/10 14:38:10
 */
@Getter
@AllArgsConstructor
public class TimeUnitLocalDateTimeCalculator implements LocalDateTimeCalculator {

    private final long amountToAdd;
    private final ChronoUnit unit;

    @Override
    public Optional<LocalDateTime> calculate(LocalDateTime base) {
        return Optional.of(base.plus(amountToAdd, unit));
    }
}
