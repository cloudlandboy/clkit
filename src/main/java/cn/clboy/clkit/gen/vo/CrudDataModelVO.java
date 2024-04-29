package cn.clboy.clkit.gen.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * crud模型dto
 *
 * @author clboy
 * @date 2024/04/23 17:27:51
 */
@Data
public class CrudDataModelVO {

    /**
     * 语言
     */
    private String lang;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 作者
     */
    private String author;

    /**
     * 额外参数
     */
    private Map<String, Object> extraParams;

    /**
     * 实体列表
     */
    private EntityInfo entity;

    /**
     * 现在日期时间
     */
    private LocalDateTime nowDateTime;

    /**
     * 现在日期时间iso格式
     */
    private String nowDateTimeIsoFormat;

    /**
     * 现在日期时间规范格式
     */
    private String nowDateTimeNormFormat;


    /**
     * 表信息
     *
     * @author clboy
     * @date 2024/04/24 09:07:11
     */
    @Data
    public static class EntityInfo {

        /**
         * 帕斯卡式名称
         */
        private String pascalCaseName;

        /**
         * 驼峰命名
         */
        private String camelCaseName;

        /**
         * 是复合ID
         */
        private Boolean compositeId;

        /**
         * id列
         */
        private List<AttributeInfo> idAttributeList;

        /**
         * 表信息
         */
        private TableBasicVO table;

        /**
         * 属性列表
         */
        private List<AttributeInfo> attributeList;

        /**
         * 导入语句列表
         */
        private List<String> importStatementList;
    }

    /**
     * 属性信息
     *
     * @author clboy
     * @date 2024/04/24 09:10:00
     */
    @Data
    public static class AttributeInfo {

        /**
         * 驼峰式名称
         */
        private String camelCaseName;

        /**
         * 类型
         */
        private String type;

        /**
         * 包路径
         */
        private String packagePath;

        /**
         * 列
         */
        private ColumnInfoVO column;


    }


}
