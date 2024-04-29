package cn.clboy.clkit.gen.repository;

import cn.clboy.clkit.gen.entity.CrudTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DB存储库
 *
 * @author clboy
 * @date 2024/04/18 16:46:29
 */
@Repository
public interface CrudTemplateRepository extends JpaRepository<CrudTemplate, Long> {

    /**
     * 按名称存在
     *
     * @param name 名称
     */
    boolean existsByName(String name);

}
