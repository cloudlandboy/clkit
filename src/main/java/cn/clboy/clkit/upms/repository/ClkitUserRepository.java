package cn.clboy.clkit.upms.repository;

import cn.clboy.clkit.common.component.jpa.BaseRepository;
import cn.clboy.clkit.upms.entity.ClkitUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * clKit用户存储库
 *
 * @author clboy
 * @date 2024/05/17 16:33:14
 */
@Repository
public interface ClkitUserRepository extends BaseRepository<ClkitUser, Long> {

    @Query(value = "SELECT clkit_user_id FROM clkit_user_role WHERE role_id = (SELECT id FROM role WHERE code = ?)",
            nativeQuery = true)
    List<Long> findUserIdByRoleCode(String roleCode);
}
