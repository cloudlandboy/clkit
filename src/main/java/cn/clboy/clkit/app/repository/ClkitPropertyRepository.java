package cn.clboy.clkit.app.repository;

import cn.clboy.clkit.app.entity.ClkitProperty;
import cn.clboy.clkit.common.component.jpa.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * clKit属性存储库
 *
 * @author clboy
 * @date 2024/05/17 15:19:55
 */
@Repository
public interface ClkitPropertyRepository extends BaseRepository<ClkitProperty, Long> {
}
