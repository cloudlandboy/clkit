package cn.clboy.clkit.common.service;

import cn.clboy.clkit.gen.entity.Db;

import java.util.List;

/**
 * 基本服务
 *
 * @author clboy
 * @date 2024/04/18 17:06:36
 */
public interface CrudService<T, ID> {

    /**
     * 保存
     *
     * @param dto DTO
     */
    T save(T dto);

    /**
     * 按ID更新
     *
     * @param dto DTO
     */
    T updateById(T dto);

    /**
     * 获取通过ID
     *
     * @param id ID
     */
    T getById(ID id);

    /**
     * 获取所有
     */
    List<T> getAll();

    /**
     * 按ID删除
     *
     * @param id ID
     */
    void removeById(ID id);

}
