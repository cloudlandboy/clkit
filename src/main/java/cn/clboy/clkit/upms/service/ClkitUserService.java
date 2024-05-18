package cn.clboy.clkit.upms.service;

import cn.clboy.clkit.common.service.CrudService;
import cn.clboy.clkit.upms.entity.ClkitUser;

import java.util.Optional;


/**
 * clKit用户服务
 *
 * @author clboy
 * @date 2024/05/17 16:32:17
 */
public interface ClkitUserService extends CrudService<ClkitUser, Long> {

    /**
     * 获取通过名称
     *
     * @param name 名称
     */
    Optional<ClkitUser> getByName(String name);
}
