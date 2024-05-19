package cn.clboy.clkit.upms.repository;

import cn.clboy.clkit.common.component.jpa.BaseRepository;
import cn.clboy.clkit.upms.entity.ClkitToken;
import org.springframework.stereotype.Repository;


/**
 * CLKIT令牌库
 *
 * @author clboy
 * @date 2024/05/17 16:33:14
 */
@Repository
public interface ClkitTokenRepository extends BaseRepository<ClkitToken, Long> {

}
