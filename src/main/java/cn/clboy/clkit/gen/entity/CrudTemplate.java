package cn.clboy.clkit.gen.entity;

import cn.clboy.clkit.common.entity.BaseEntity;
import cn.clboy.clkit.common.constants.enums.TemplateEngineEnum;
import cn.clboy.clkit.common.entity.IUniqueNameEntity;
import cn.clboy.clkit.gen.jpa.CrudTemplateModuleListAttributeConverter;
import cn.clboy.clkit.gen.jpa.ExtraParamListAttributeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Crud模板
 *
 * @author clboy
 * @date 2024/04/23 15:39:01
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class CrudTemplate extends BaseEntity implements IUniqueNameEntity {

    /**
     * 唯一名称
     */
    @Comment("唯一名称")
    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * 语言
     */
    @Comment("语言")
    @NotBlank
    private String lang;

    /**
     * 模板引擎
     */
    @Comment("模板引擎")
    @NotNull
    private TemplateEngineEnum engine;

    /**
     * 已锁定
     */
    @Comment("已锁定")
    @NotNull
    private Boolean locked;

    /**
     * 模块文件名格式化
     */
    @Comment("模块文件名格式化")
    private String moduleFileNameFormat;

    /**
     * 扩展参数列表
     */
    @Lob
    @Comment("扩展参数列表")
    @Convert(converter = ExtraParamListAttributeConverter.class)
    private List<ExtraParam> extraParams;

    /**
     * 模块列表
     */
    @Lob
    @Comment("模块列表")
    @NotEmpty
    @Convert(converter = CrudTemplateModuleListAttributeConverter.class)
    private List<CrudTemplateModule> modules;


    /**
     * 额外参数
     *
     * @author clboy
     * @date 2024/04/23 15:45:25
     */
    @Data
    public static class ExtraParam {

        /**
         * 名称
         */
        private String name;

        /**
         * key
         */
        private String key;

        /**
         * 类型
         */
        private String type;

        /**
         * 枚举值列表
         */
        private String enumList;

        /**
         * 默认值
         */
        private String defaultValue;

        /**
         * 必填
         */
        private Boolean required;

    }

    /**
     * Crud模板模块
     *
     * @author clboy
     * @date 2024/04/23 15:45:44
     */
    @Data
    public static class CrudTemplateModule {

        /**
         * 名称
         */
        private String name;

        /**
         * 模板
         */
        private String template;

        /**
         * 文件名格式化
         */
        private String fileNameFormat;

        /**
         * 生成时跳过
         */
        private Boolean skip;
    }
}
