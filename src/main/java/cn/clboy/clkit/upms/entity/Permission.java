package cn.clboy.clkit.upms.entity;

import cn.clboy.clkit.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 权限
 *
 * @author clboy
 * @date 2024/05/17 11:33:58
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseEntity {

    /**
     * 名称
     */
    @Comment("名称")
    @Column(nullable = false)
    private String name;

    /**
     * 编码
     */
    @Comment("编码")
    @Column(nullable = false, unique = true)
    private String code;
}
