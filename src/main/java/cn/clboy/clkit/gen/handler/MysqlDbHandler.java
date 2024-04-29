package cn.clboy.clkit.gen.handler;

import cn.clboy.clkit.common.constants.enums.DbPlatformEnum;
import cn.clboy.clkit.gen.entity.Db;
import cn.clboy.clkit.gen.vo.ColumnInfoVO;
import cn.clboy.clkit.gen.vo.TableBasicVO;
import cn.clboy.clkit.gen.vo.TableInfoVO;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * mysql DB处理程序
 *
 * @author clboy
 * @date 2024/04/19 09:11:40
 */
@Component
public class MysqlDbHandler extends AbstractDbHandler {

    @Override
    public DbPlatformEnum getPlatform() {
        return DbPlatformEnum.MYSQL;
    }

    @Override
    public DataSource buildDataSource(Db db, boolean forMetadata) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(db.getUsername());
        dataSource.setPassword(db.getPassword());
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        StringBuilder urlBuilder = new StringBuilder("jdbc:mysql://");
        urlBuilder.append(db.getHost());
        urlBuilder.append(":").append(db.getPort());
        urlBuilder.append("/");
        if (forMetadata) {
            urlBuilder.append("information_schema");
        } else {
            urlBuilder.append(db.getDatabase());
        }
        dataSource.setJdbcUrl(urlBuilder.toString());
        return dataSource;
    }

    @Override
    public List<TableBasicVO> queryTable(JdbcTemplate jdbcTemplate, String database, String keyword) {
        List<Object> args = new ArrayList<>(3);
        StringBuilder sqlBuilder = new StringBuilder("SELECT table_name AS name,table_comment AS comment FROM TABLES WHERE table_schema = ?");
        args.add(database);
        if (StringUtils.hasText(keyword)) {
            sqlBuilder.append(" AND (table_name LIKE CONTACT(%,?,%) OR table_comment LIKE CONTACT(%,?,%)) ");
            args.add(keyword);
            args.add(keyword);
        }

        return jdbcTemplate.query(sqlBuilder.toString(), BeanPropertyRowMapper.newInstance(TableBasicVO.class), args.toArray());
    }

    @Override
    public List<TableInfoVO> queryTableInfo(JdbcTemplate jdbcTemplate, String database, List<String> tableNames) {
        StringBuilder tableSqlBuilder = new StringBuilder("SELECT table_name AS name,table_comment AS comment FROM TABLES" +
                " WHERE table_schema = ?");
        List<Object> argsBuilder = new ArrayList<>(tableNames.size() + 1);
        argsBuilder.add(database);
        String tableNameInSql = "";
        if (!CollectionUtils.isEmpty(tableNames)) {
            tableSqlBuilder.append(" AND table_name IN ");
            StringJoiner inJoiner = new StringJoiner(",", "(", ")");
            for (String tableName : tableNames) {
                inJoiner.add("?");
                argsBuilder.add(tableName);
            }
            tableSqlBuilder.append(inJoiner);
            tableNameInSql = inJoiner.toString();
        }

        Object[] args = argsBuilder.toArray();
        List<TableInfoVO> tableInfoList = jdbcTemplate.query(tableSqlBuilder.toString(),
                BeanPropertyRowMapper.newInstance(TableInfoVO.class), args);
        if (CollectionUtils.isEmpty(tableInfoList)) {
            return Collections.emptyList();
        }

        Map<String, TableInfoVO> tableInfoMap = tableInfoList.stream()
                .collect(Collectors.toMap(TableInfoVO::getName, Function.identity()));


        StringBuilder columnSqlBuilder = new StringBuilder("SELECT table_name AS table_name,");
        columnSqlBuilder.append("column_name AS name,")
                .append("column_comment AS comment,")
                .append("column_key AS index_desc,")
                .append("is_nullable AS nullable,")
                .append("column_type AS data_type,")
                .append("character_maximum_length AS max_length,")
                .append("numeric_precision AS max_int_digit,")
                .append("numeric_scale AS max_fraction_digit,")
                .append("extra")
                .append(" FROM COLUMNS WHERE table_schema = ?");
        if (StringUtils.hasText(tableNameInSql)) {
            columnSqlBuilder.append(" AND table_name IN ").append(tableNameInSql);
        }
        columnSqlBuilder.append(" ORDER BY ordinal_position");

        Map<String, List<ColumnInfoVO>> tableColumnGroup = jdbcTemplate.query(columnSqlBuilder.toString(),
                        BeanPropertyRowMapper.newInstance(ColumnInfoVO.class), args)
                .stream().peek(ci -> ci.setIdc("PRI".equalsIgnoreCase(ci.getIndexDesc())))
                .collect(Collectors.groupingBy(ColumnInfoVO::getTableName));

        tableColumnGroup.forEach((tableName, columnInfoList) -> {
            TableInfoVO tableInfo = tableInfoMap.get(tableName);
            tableInfo.setColumnList(columnInfoList);
        });

        return tableInfoList;
    }
}
