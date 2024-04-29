package cn.clboy.clkit.os.handler;

import org.springframework.beans.factory.InitializingBean;

/**
 * 抽象操作系统处理程序
 *
 * @author clboy
 * @date 2024/04/22 13:43:37
 */
public abstract class AbstractOsHandler implements OsHandler, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
