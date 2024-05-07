package cn.clboy.clkit.extension.entity;

import cn.clboy.clkit.common.entity.BaseEntity;
import cn.clboy.clkit.common.entity.IUniqueNameEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 扩展s
 *
 * @author clboy
 * @date 2024/05/06 11:07:36
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Extension extends BaseEntity implements IUniqueNameEntity {

    /**
     * 文件夹id
     */
    @Comment("文件夹id")
    @Column(nullable = false)
    @NotNull(message = "invalid folderId")
    private Long folderId;

    /**
     * 名称
     */
    @Comment("名称")
    @Column(unique = true, nullable = false)
    @NotBlank(message = "invalid name")
    private String name;

    /**
     * 类型
     */
    @Comment("类型")
    @Column(nullable = false)
    @NotBlank(message = "invalid type")
    private String type;

    /**
     * URL
     */
    @Comment("URL")
    private String url;

    /**
     * 主页
     */
    @Comment("主页")
    private String index;

    /**
     * 排序值
     */
    @Comment("排序值")
    @Column(nullable = false)
    @NotNull(message = "invalid sortValue")
    private Integer sortValue;

    /**
     * 隐藏
     */
    @Comment("隐藏")
    @Column(nullable = false)
    @NotNull(message = "invalid hide")
    private Boolean hide;

    /**
     * 访问路径
     */
    @Comment("访问路径")
    private String path;
}
