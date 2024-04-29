package cn.clboy.clkit.gen.handler;

import org.springframework.beans.factory.InitializingBean;

/**
 * 抽象数据库处理程序
 *
 * @author clboy
 * @date 2024/04/19 09:10:02
 */
public abstract class AbstractDbHandler implements DbHandler, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        DbHandler.HOLDER.addHandler(this.getPlatform(), this);
    }
}
