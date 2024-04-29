package cn.clboy.clkit.gen.service.impl;

import cn.clboy.clkit.common.constants.enums.DbPlatformEnum;
import cn.clboy.clkit.common.service.AppDataHandlerCrudServiceImpl;
import cn.clboy.clkit.gen.entity.Db;
import cn.clboy.clkit.gen.handler.DbHandler;
import cn.clboy.clkit.gen.repository.DbRepository;
import cn.clboy.clkit.gen.service.DbService;
import cn.clboy.clkit.gen.vo.TableBasicVO;
import cn.clboy.clkit.gen.vo.TableInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库服务实施
 *
 * @author clboy
 * @date 2024/04/18 16:45:06
 */
@Service
@RequiredArgsConstructor
public class DbServiceImpl extends AppDataHandlerCrudServiceImpl<Db, DbRepository> implements DbService {

    @Override
    public Db save(Db dto) {
        boolean exists = this.repository.existsByName(dto.getName());
        Assert.isTrue(!exists, "唯一名称已经存在");
        return this.repository.save(dto);
    }

    @Override
    public Db updateById(Db dto) {
        Db old = this.getById(dto.getId());
        if (!old.getName().equals(dto.getName())) {
            boolean exists = this.repository.existsByName(dto.getName());
            Assert.isTrue(!exists, "唯一名称已经存在");
        }
        return this.repository.save(dto);
    }

    @Override
    public List<TableBasicVO> queryTable(Long id, String keyword) {
        Db db = this.getById(id);
        DbHandler handler = DbHandler.HOLDER.getHandler(db.getPlatform());
        DataSource dataSource = null;
        try {
            dataSource = handler.buildDataSource(db, true);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            Assert.notNull(handler, "不支持的数据库类型");
            return handler.queryTable(jdbcTemplate, db.getDatabase(), keyword);
        } finally {
            handler.closeDatasource(dataSource);
        }

    }

    @Override
    public List<TableInfoVO> queryTableInfo(Db db, List<String> tableNames) {
        DbHandler handler = DbHandler.HOLDER.getHandler(db.getPlatform());
        DataSource dataSource = null;
        try {
            dataSource = handler.buildDataSource(db, true);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            Assert.notNull(handler, "不支持的数据库类型");
            return handler.queryTableInfo(jdbcTemplate, db.getDatabase(), tableNames);
        } finally {
            handler.closeDatasource(dataSource);
        }

    }

    @Override
    public List<String> getSupportedPlatform() {
        return Arrays.stream(DbPlatformEnum.values()).map(Enum::name).collect(Collectors.toList());
    }

}
