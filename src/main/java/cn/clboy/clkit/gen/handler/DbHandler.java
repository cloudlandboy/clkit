package cn.clboy.clkit.gen.handler;

import cn.clboy.clkit.common.constants.enums.DbPlatformEnum;
import cn.clboy.clkit.common.holder.HandlerHolder;
import cn.clboy.clkit.gen.entity.Db;
import cn.clboy.clkit.gen.vo.TableBasicVO;
import cn.clboy.clkit.gen.vo.TableInfoVO;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.Closeable;
import java.util.List;

/**
 * 数据库处理程序
 *
 * @author clboy
 * @date 2024/04/18 17:51:46
 */
public interface DbHandler {

    HandlerHolder<DbPlatformEnum, DbHandler> HOLDER = new HandlerHolder<>();


    /**
     * 获取平台
     */
    DbPlatformEnum getPlatform();

    /**
     * 构建数据源,元数据
     *
     * @param db          DB
     * @param forMetadata 用于元数据
     */
    DataSource buildDataSource(Db db, boolean forMetadata);

    /**
     * 查询表
     *
     * @param jdbcTemplate jdbc模板
     * @param keyword      关键字
     */
    List<TableBasicVO> queryTable(JdbcTemplate jdbcTemplate, String database, String keyword);

    /**
     * 查询表信息
     *
     * @param jdbcTemplate jdbc模板
     * @param database     数据库
     * @param tableNames   表名
     */
    List<TableInfoVO> queryTableInfo(JdbcTemplate jdbcTemplate, String database, List<String> tableNames);

    /**
     * 关闭数据源
     *
     * @param dataSource 数据源
     */
    @SneakyThrows
    default void closeDatasource(DataSource dataSource) {
        if (dataSource == null) {
            return;
        }
        if (dataSource instanceof Closeable) {
            ((Closeable) dataSource).close();
            return;
        }
        throw new UnsupportedOperationException();

    }

}
