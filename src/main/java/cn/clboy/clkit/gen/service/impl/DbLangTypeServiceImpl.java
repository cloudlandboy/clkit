package cn.clboy.clkit.gen.service.impl;

import cn.clboy.clkit.app.handler.AppDataHandler;
import cn.clboy.clkit.common.constants.enums.DbPlatformEnum;
import cn.clboy.clkit.common.service.AppDataHandlerCrudServiceImpl;
import cn.clboy.clkit.gen.entity.DbLangType;
import cn.clboy.clkit.gen.repository.DbLangTypeRepository;
import cn.clboy.clkit.gen.service.DbLangTypeService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库类型服务实施
 *
 * @author clboy
 * @date 2024/04/18 17:19:42
 */
@Service
public class DbLangTypeServiceImpl extends AppDataHandlerCrudServiceImpl<DbLangType, DbLangTypeRepository>
        implements DbLangTypeService, AppDataHandler<DbLangType> {

    @Override
    public DbLangType save(DbLangType dto) {
        boolean exists = this.repository.existsByName(dto.getName());
        Assert.isTrue(!exists, "唯一名称已经存在");
        return this.repository.save(dto);
    }

    @Override
    public DbLangType updateById(DbLangType dto) {
        DbLangType old = this.getById(dto.getId());
        if (!old.getName().equals(dto.getName())) {
            boolean exists = this.repository.existsByName(dto.getName());
            Assert.isTrue(!exists, "唯一名称已经存在");
        }
        return this.repository.save(dto);
    }

    @Override
    public void unlockById(Long id) {
        DbLangType dbLangType = this.getById(id);
        dbLangType.setLocked(Boolean.FALSE);
        this.updateById(dbLangType);
    }

    @Override
    public List<DbLangType> find(String dbPlatform, String langType) {
        try {
            DbPlatformEnum dbPlatformEnum = DbPlatformEnum.valueOf(dbPlatform);
            return this.repository.findByDbPlatformAndLangType(dbPlatformEnum, langType);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("不支持的数据库平台");
        }
    }

    @Override
    public List<String> getAllLang() {
        return this.getAll().stream().map(DbLangType::getLangType).distinct().collect(Collectors.toList());
    }
}
