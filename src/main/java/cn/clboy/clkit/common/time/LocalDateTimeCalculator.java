package cn.clboy.clkit.common.time;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 本地日期时间计算器
 *
 * @author clboy
 * @date 2024/05/10 14:35:40
 */
public interface LocalDateTimeCalculator {
    LocalDateTimeCalculator EMPTY = t -> Optional.empty();
    LocalDateTimeCalculator IDENTITY = Optional::of;

    /**
     * 计算
     *
     * @param base 基地
     */
    Optional<LocalDateTime> calculate(LocalDateTime base);
}
