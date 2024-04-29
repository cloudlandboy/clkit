package cn.clboy.clkit.gen.service.impl;

import cn.clboy.clkit.common.service.AppDataHandlerCrudServiceImpl;
import cn.clboy.clkit.gen.entity.CrudTemplate;
import cn.clboy.clkit.gen.repository.CrudTemplateRepository;
import cn.clboy.clkit.gen.service.CrudTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * curd模板服务impl
 *
 * @author clboy
 * @date 2024/04/23 16:13:33
 */
@Service
public class CurdTemplateServiceImpl extends AppDataHandlerCrudServiceImpl<CrudTemplate, CrudTemplateRepository>
        implements CrudTemplateService {

    @Override
    public CrudTemplate save(CrudTemplate dto) {
        boolean exists = this.repository.existsByName(dto.getName());
        Assert.isTrue(!exists, "唯一名称已经存在");
        return this.repository.save(dto);
    }

    @Override
    public CrudTemplate updateById(CrudTemplate dto) {
        CrudTemplate old = this.getById(dto.getId());
        if (!old.getName().equals(dto.getName())) {
            boolean exists = this.repository.existsByName(dto.getName());
            Assert.isTrue(!exists, "唯一名称已经存在");
        }
        return this.repository.save(dto);
    }

    @Override
    public void unlockById(Long id) {
        CrudTemplate template = getById(id);
        template.setLocked(Boolean.FALSE);
        this.repository.save(template);
    }
}
