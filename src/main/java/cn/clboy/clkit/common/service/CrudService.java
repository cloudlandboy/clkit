package cn.clboy.clkit.common.service;

import java.util.Collection;
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
     * 批量获取通过ID
     *
     * @param ids ids
     */
    List<T> getByIds(Collection<ID> ids);

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
