package cn.clboy.clkit.gen.repository;

import cn.clboy.clkit.common.constants.enums.DbPlatformEnum;
import cn.clboy.clkit.gen.entity.DbLangType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 数据库类型存储库
 *
 * @author clboy
 * @date 2024/04/18 17:19:15
 */
public interface DbLangTypeRepository extends JpaRepository<DbLangType, Long> {

    /**
     * 按数据平台和语言类型查找
     *
     * @param dbPlatform 数据库平台
     * @param langType   语言类型
     */
    List<DbLangType> findByDbPlatformAndLangType(DbPlatformEnum dbPlatform, String langType);

    /**
     * 按名称存在
     *
     * @param name 名称
     */
    boolean existsByName(String name);
}
