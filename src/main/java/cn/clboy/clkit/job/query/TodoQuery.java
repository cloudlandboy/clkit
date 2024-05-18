package cn.clboy.clkit.job.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 待办查询
 *
 * @author clboy
 * @date 2024/05/10 15:39:42
 */
@Data
@Schema(name = "待办查询")
public class TodoQuery {

    /**
     * 状态
     */
    @Schema(name = "状态")
    private String status;

    /**
     * 开始截止日期
     */
    @Schema(name = "截止日期-开始")
    private LocalDate startDeadlineDate;

    /**
     * 结束截止日期
     */
    @Schema(name = "截止日期-结束")
    private LocalDate endDeadlineDate;
}
