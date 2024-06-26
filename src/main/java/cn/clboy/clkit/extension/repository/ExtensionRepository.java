package cn.clboy.clkit.extension.repository;

import cn.clboy.clkit.common.component.jpa.BaseRepository;
import cn.clboy.clkit.extension.entity.Extension;
import org.springframework.stereotype.Repository;

/**
 * 扩展Repository
 *
 * @author clboy
 * @date 2024/05/06 11:34:41
 */
@Repository
public interface ExtensionRepository extends BaseRepository<Extension, Long> {
}
