package cn.clboy.clkit.code.service;

import cn.clboy.clkit.code.entity.Regexp;
import cn.clboy.clkit.common.service.CrudService;

import java.util.List;

/**
 * 正则服务
 *
 * @author clboy
 * @date 2024/04/18 16:44:51
 */
public interface RegexpService extends CrudService<Regexp, Long> {

    /**
     * 下载任何规则js
     */
    String downloadAnyRuleJs();

    /**
     * 导入数据
     *
     * @param dataList 数据列表
     */
    void importData(List<Regexp> dataList);
}
