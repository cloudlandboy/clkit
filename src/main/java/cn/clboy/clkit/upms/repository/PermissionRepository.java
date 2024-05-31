package cn.clboy.clkit.upms.repository;

import cn.clboy.clkit.common.component.jpa.BaseRepository;
import cn.clboy.clkit.upms.entity.Permission;
import org.springframework.stereotype.Repository;


/**
 * 权限库
 *
 * @author clboy
 * @date 2024/05/20 15:34:52
 */
@Repository
public interface PermissionRepository extends BaseRepository<Permission, Long> {

}
