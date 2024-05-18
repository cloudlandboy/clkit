package cn.clboy.clkit.app.service;

import cn.clboy.clkit.app.entity.ClkitProperty;
import cn.clboy.clkit.common.service.CrudService;

/**
 * 配置服务
 *
 * @author clboy
 * @date 2024/05/17 15:16:47
 */
public interface ClkitPropertyService extends CrudService<ClkitProperty, Long> {

    /**
     * 获取通过关键
     *
     * @param key 关键
     */
    ClkitProperty getByKey(String key);
}
