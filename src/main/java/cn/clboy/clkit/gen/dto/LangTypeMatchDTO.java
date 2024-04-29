package cn.clboy.clkit.gen.dto;

import lombok.Data;

/**
 * 语言类型匹配dto
 *
 * @author clboy
 * @date 2024/04/18 15:51:29
 */
@Data
public class LangTypeMatchDTO {

    /**
     * 匹配正则
     */
    private String match;

    /**
     * 语言类型
     */
    private String type;

    /**
     * 包路径
     */
    private String packagePath;

    /**
     * 导入语句
     */
    private String importStatement;

    /**
     * 必须配置项，.*
     */
    private Boolean required;
}
