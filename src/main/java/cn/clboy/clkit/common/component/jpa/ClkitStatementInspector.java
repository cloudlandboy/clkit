package cn.clboy.clkit.common.component.jpa;

import org.hibernate.resource.jdbc.spi.StatementInspector;

/**
 * hibernate sql拦截器
 *
 * @author clboy
 * @date 2024/05/21 16:47:29
 */
public class ClkitStatementInspector implements StatementInspector {

    public ClkitStatementInspector() {
    }

    @Override
    public String inspect(String sql) {
        return null;
    }
}
