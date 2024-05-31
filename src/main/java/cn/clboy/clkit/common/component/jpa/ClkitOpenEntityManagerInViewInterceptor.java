package cn.clboy.clkit.common.component.jpa;

import cn.clboy.clkit.common.component.security.ClkitAuthUser;
import cn.clboy.clkit.common.constants.ClkitConstant;
import cn.clboy.clkit.common.util.SecurityUtils;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;

/**
 * clKit视图中开放实体管理器拦截器
 *
 * @author clboy
 * @date 2024/05/21 18:00:25
 */
public class ClkitOpenEntityManagerInViewInterceptor extends OpenEntityManagerInViewInterceptor {
    @Override
    @SuppressWarnings("all")
    public void preHandle(WebRequest request) throws DataAccessException {
        super.preHandle(request);
        EntityManagerHolder holder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(obtainEntityManagerFactory());
        Session hibernateSession = holder.getEntityManager().unwrap(Session.class);
        hibernateSession.enableFilter(ClkitConstant.HIBERNATE_USER_DATA_FILTER_NAME)
                .setParameter(ClkitConstant.USER_ID_FIELD_NAME, Optional.ofNullable(SecurityUtils.getLoginUser())
                        .map(ClkitAuthUser::getUserId).orElse(0L));
    }
}
