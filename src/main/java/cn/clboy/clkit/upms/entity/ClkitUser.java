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
 * 用户
 *
 * @author clboy
 * @date 2024/05/17 09:26:20
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class ClkitUser extends BaseEntity implements IUniqueNameEntity {

    /**
     * 用户名
     */
    @Comment("用户名")
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 昵称
     */
    @Comment("昵称")
    @Column(nullable = false)
    private String nickname;

    /**
     * 真实姓名
     */
    @Comment("真实姓名")
    @Column(nullable = false)
    private String realName;

    /**
     * 密码
     */
    @Comment("密码")
    @Column(nullable = false)
    private String password;

    /**
     * 邮箱
     */
    @Comment("邮箱")
    private String email;

    /**
     * 角色
     */
    @ManyToMany
    private List<Role> role;

}
