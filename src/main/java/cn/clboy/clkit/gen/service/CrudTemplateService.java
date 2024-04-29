package cn.clboy.clkit.gen.service;

import cn.clboy.clkit.common.service.CrudService;
import cn.clboy.clkit.gen.entity.CrudTemplate;

/**
 * curd模板服务
 *
 * @author clboy
 * @date 2024/04/23 16:13:13
 */
public interface CrudTemplateService extends CrudService<CrudTemplate, Long> {
    void unlockById(Long id);

}
