package cn.clboy.clkit.gen.service;

import cn.clboy.clkit.common.service.CrudService;
import cn.clboy.clkit.gen.entity.DbLangType;

import java.util.List;

/**
 * 数据库类型服务
 *
 * @author clboy
 * @date 2024/04/18 17:17:38
 */
public interface DbLangTypeService extends CrudService<DbLangType, Long> {

    /**
     * 通过ID解锁
     *
     * @param id ID
     */
    void unlockById(Long id);

    /**
     * 获取通过DB平台语言类型查找
     *
     * @param dbPlatform 数据库平台
     * @param langType   语言类型
     */
    List<DbLangType> find(String dbPlatform, String langType);

    /**
     * 获取所有语言
     */
    List<String> getAllLang();
}
