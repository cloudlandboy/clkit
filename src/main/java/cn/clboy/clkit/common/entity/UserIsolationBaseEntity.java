package cn.clboy.clkit.common.entity;

import cn.clboy.clkit.common.component.security.ClkitAuthUser;
import cn.clboy.clkit.common.constants.ClkitConstant;
import cn.clboy.clkit.common.util.SecurityUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * 用户隔离基础实体
 *
 * @author clboy
 * @date 2024/05/21 16:42:18
 */
@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@FilterDef(
        name = ClkitConstant.HIBERNATE_USER_DATA_FILTER_NAME,
        defaultCondition = ClkitConstant.USER_ID_COLUMN_NAME + "=:" + ClkitConstant.USER_ID_FIELD_NAME,
        parameters = @ParamDef(name = ClkitConstant.USER_ID_FIELD_NAME, type = "long")
)
@Filter(name = ClkitConstant.HIBERNATE_USER_DATA_FILTER_NAME)
public class UserIsolationBaseEntity extends BaseEntity {

    /**
     * 用户id
     */
    @Comment("用户id")
    @Column(nullable = false, updatable = false)
    private Long userId;

    @Override
    public void prePersist() {
        super.prePersist();
        if (!SecurityUtils.isRunWithInnerEnv()) {
            //需要自己设置
            ClkitAuthUser user = SecurityUtils.getLoginUserNonNull();
            this.setUserId(user.getUserId());
        }
    }
}
