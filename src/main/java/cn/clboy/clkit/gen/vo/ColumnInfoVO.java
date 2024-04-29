package cn.clboy.clkit.gen.vo;

import lombok.Data;

/**
 * 列信息数据
 *
 * @author clboy
 * @date 2024/04/23 16:29:24
 */
@Data
public class ColumnInfoVO {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 名称
     */
    private String name;

    /**
     * 是id列
     */
    private Boolean idc;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 注释
     */
    private String comment;

    /**
     * 索引描述
     */
    private String indexDesc;

    /**
     * 可以为null
     */
    private Boolean nullable;

    /**
     * 最大长度
     */
    private Long maxLength;

    /**
     * 最大整数位
     */
    private Long maxIntDigit;

    /**
     * 最大小数位
     */
    private Long maxFractionDigit;

    /**
     * 是无符号数
     */
    private Boolean unsignedNumber;

    /**
     * 额外信息
     */
    private String extra;
}
