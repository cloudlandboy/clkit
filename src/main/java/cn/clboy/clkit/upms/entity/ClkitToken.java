package cn.clboy.clkit.upms.entity;

import cn.clboy.clkit.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * clKit令牌
 *
 * @author clboy
 * @date 2024/05/18 21:11:47
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class ClkitToken extends BaseEntity {

    /**
     * 用户id
     */
    @Comment("用户id")
    @Column(nullable = false)
    private Long userId;

    /**
     * 访问令牌
     */
    @Comment("访问令牌")
    @Column(nullable = false, unique = true)
    private String accessToken;

    /**
     * 刷新令牌
     */
    @Comment("刷新令牌")
    @Column(nullable = false, unique = true)
    private String refreshToken;

    /**
     * 访问令牌到期时间
     */
    @Comment("访问令牌到期时间")
    @Column(nullable = false)
    private LocalDateTime accessTokenExpireTime;

    /**
     * 刷新令牌到期时间
     */
    @Comment("刷新令牌到期时间")
    @Column(nullable = false)
    private LocalDateTime refreshTokenExpireTime;

    @Override
    @JsonIgnore
    public Long getId() {
        return super.getId();
    }
}
