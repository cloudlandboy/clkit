package cn.clboy.clkit.app.entity;

import cn.clboy.clkit.common.entity.BaseEntity;
import cn.clboy.clkit.common.entity.IUniqueNameEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 配置
 *
 * @author clboy
 * @date 2024/05/17 15:10:23
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class ClkitProperty extends BaseEntity implements IUniqueNameEntity {

    /**
     * 名称
     */
    @Comment("名称")
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 属性键
     */
    @Comment("属性键")
    @Column(nullable = false, unique = true)
    private String propKey;

    /**
     * 属性值
     */
    @Comment("属性值")
    private String propValue;

    /**
     * 说明
     */
    @Comment("说明")
    private String description;

    /**
     * 内置的
     */
    @Comment("内置的")
    @Column(nullable = false)
    private Boolean internal;

    /**
     * 可编辑
     */
    @Comment("可编辑")
    @Column(nullable = false)
    private Boolean editable;
}
