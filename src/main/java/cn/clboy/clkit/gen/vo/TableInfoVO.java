package cn.clboy.clkit.gen.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 表信息数据
 *
 * @author clboy
 * @date 2024/04/23 16:29:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TableInfoVO extends TableBasicVO {

    /**
     * 列表
     */
    private List<ColumnInfoVO> columnList;
}
