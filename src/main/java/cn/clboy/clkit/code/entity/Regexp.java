package cn.clboy.clkit.code.entity;

import cn.clboy.clkit.common.component.jpa.StringListAttributeConverter;
import cn.clboy.clkit.common.entity.BaseEntity;
import cn.clboy.clkit.common.entity.IUniqueNameEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 正则表达式
 *
 * @author clboy
 * @date 2024/06/03 17:15:10
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Regexp extends BaseEntity implements IUniqueNameEntity {

    /**
     * 唯一名称
     */
    @NotBlank
    @Comment("唯一名称")
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * 描述
     */
    @NotBlank
    @Comment("描述")
    private String description;

    /**
     * 正则表达式
     */
    @Lob
    @NotBlank
    @Comment("正则表达式")
    private String regex;

    /**
     * 示例
     */
    @Lob
    @Comment("示例")
    @Convert(converter = StringListAttributeConverter.class)
    private List<String> examples;

    /**
     * 反例
     */
    @Lob
    @Comment("反例")
    @Convert(converter = StringListAttributeConverter.class)
    private List<String> counterExamples;

}
