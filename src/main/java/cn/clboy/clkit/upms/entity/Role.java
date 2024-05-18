package cn.clboy.clkit.upms.entity;

import cn.clboy.clkit.common.entity.BaseEntity;
import cn.clboy.clkit.common.entity.IUniqueNameEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * 角色
 *
 * @author clboy
 * @date 2024/05/17 11:33:25
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity implements IUniqueNameEntity {

    /**
     * 名称
     */
    @Comment("名称")
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 编码
     */
    @Comment("编码")
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * 权限
     */
    @ManyToMany
    private List<Permission> permission;
}
