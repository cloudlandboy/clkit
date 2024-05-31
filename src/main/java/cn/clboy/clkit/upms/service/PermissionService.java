package cn.clboy.clkit.upms.service;

import cn.clboy.clkit.common.service.CrudService;
import cn.clboy.clkit.upms.entity.Permission;
import cn.hutool.core.lang.tree.Tree;

import java.util.List;


/**
 * 权限服务
 *
 * @author clboy
 * @date 2024/05/20 15:34:29
 */
public interface PermissionService extends CrudService<Permission, Long> {

    /**
     * 获取权限js声明代码
     */
    String getPermissionJsConstDeclare();

    /**
     * 获取tree
     */
    List<Tree<Long>> getTree();

}
