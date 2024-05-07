package cn.clboy.clkit.extension.service;

import cn.clboy.clkit.common.service.CrudService;
import cn.clboy.clkit.extension.entity.Extension;
import cn.clboy.clkit.extension.vo.ExtensionTypeVO;
import cn.hutool.core.lang.tree.Tree;

import java.util.List;

/**
 * 扩展服务
 *
 * @author clboy
 * @date 2024/05/06 10:24:12
 */
public interface ExtensionService extends CrudService<Extension, Long> {

    /**
     * 获取类型
     */
    List<ExtensionTypeVO> getTypes();

    /**
     * 获取树
     */
    List<Tree<Long>> getTree(Boolean filterInstalled);

    /**
     * 安装
     *
     * @param id ID
     */
    void install(Long id);
}
