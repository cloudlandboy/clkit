package cn.clboy.clkit.common.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 基本实体
 *
 * @author clboy
 * @date 2024/04/18 15:13:24
 */
@Data
@MappedSuperclass
public class BaseEntity {

    /**
     * ID
     */
    @Id
    @Comment("ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Comment("创建时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Comment("更新时间")
    @Column(nullable = false)
    private LocalDateTime updatedTime;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdTime = now;
        this.updatedTime = now;
        if (this instanceof IVersionEntity) {
            ((IVersionEntity) this).setVersion(1);
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }
}
