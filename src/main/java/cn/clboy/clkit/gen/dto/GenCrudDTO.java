package cn.clboy.clkit.gen.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 生成Crud DTO
 *
 * @author clboy
 * @date 2024/04/23 15:24:58
 */
@Data
public class GenCrudDTO {

    /**
     * 作者
     */
    private String author;

    /**
     * 数据库id
     */
    private Long dbId;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 语言映射id
     */
    private Long dbLangTypeId;

    /**
     * 表名
     */
    private List<String> tableNames;

    /**
     * 额外参数
     */
    private Map<String, Object> extraParams;
}
