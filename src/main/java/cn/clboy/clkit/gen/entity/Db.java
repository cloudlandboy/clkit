package cn.clboy.clkit.gen.entity;

import cn.clboy.clkit.common.entity.BaseEntity;
import cn.clboy.clkit.common.constants.enums.DbPlatformEnum;
import cn.clboy.clkit.common.entity.IUniqueNameEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DB
 *
 * @author clboy
 * @date 2024/04/18 16:35:00
 */
@Data
@Entity
@Table()
@EqualsAndHashCode(callSuper = true)
public class Db extends BaseEntity implements IUniqueNameEntity {

    /**
     * 唯一名称
     */
    @Comment("唯一名称")
    @NotBlank(message = "invalid name")
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * 数据库平台
     */
    @Comment("数据库平台")
    @Column(nullable = false)
    @NotNull(message = "invalid platform")
    private DbPlatformEnum platform;

    /**
     * 主机地址
     */
    @Comment("主机地址")
    @Column(nullable = false)
    @NotBlank(message = "invalid host")
    private String host;

    /**
     * 连接端口
     */
    @Comment("连接端口")
    @Column(nullable = false)
    @NotNull(message = "invalid port")
    private Integer port;

    /**
     * 用户名
     */
    @Comment("用户名")
    @Column(nullable = false)
    @NotBlank(message = "invalid username")
    private String username;

    /**
     * 密码
     */
    @Comment("密码")
    @Column(nullable = false)
    @NotBlank(message = "invalid password")
    private String password;

    /**
     * 数据库
     */
    @Comment("数据库")
    @Column(nullable = false)
    @NotBlank(message = "invalid database")
    private String database;
}
