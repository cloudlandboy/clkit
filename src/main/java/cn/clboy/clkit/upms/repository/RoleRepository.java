package cn.clboy.clkit.upms.repository;

import cn.clboy.clkit.common.component.jpa.BaseRepository;
import cn.clboy.clkit.upms.entity.Role;
import org.springframework.stereotype.Repository;


/**
 * 角色存储库
 *
 * @author clboy
 * @date 2024/05/27 10:32:54
 */
@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {

}
