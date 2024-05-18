package cn.clboy.clkit.upms.repository;

import cn.clboy.clkit.common.component.jpa.BaseRepository;
import cn.clboy.clkit.upms.entity.ClkitUser;
import org.springframework.stereotype.Repository;


/**
 * clKit用户存储库
 *
 * @author clboy
 * @date 2024/05/17 16:33:14
 */
@Repository
public interface ClkitUserRepository extends BaseRepository<ClkitUser, Long> {

}
