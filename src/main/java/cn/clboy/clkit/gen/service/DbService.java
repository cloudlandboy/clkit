package cn.clboy.clkit.gen.service;

import cn.clboy.clkit.common.service.CrudService;
import cn.clboy.clkit.gen.entity.Db;
import cn.clboy.clkit.gen.vo.TableBasicVO;
import cn.clboy.clkit.gen.vo.TableInfoVO;

import java.util.List;

/**
 * db服务
 *
 * @author clboy
 * @date 2024/04/18 16:44:51
 */
public interface DbService extends CrudService<Db, Long> {

    /**
     * 查询表
     *
     * @param id      ID
     * @param keyword 关键字
     */
    List<TableBasicVO> queryTable(Long id, String keyword);

    /**
     * 查询表信息
     *
     * @param db         DB
     * @param tableNames 表名
     */
    List<TableInfoVO> queryTableInfo(Db db, List<String> tableNames);

    /**
     * 获取支持平台
     */
    List<String> getSupportedPlatform();

}
