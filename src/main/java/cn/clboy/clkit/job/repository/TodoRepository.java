package cn.clboy.clkit.job.repository;

import cn.clboy.clkit.common.component.jpa.BaseRepository;
import cn.clboy.clkit.job.entity.Todo;
import org.springframework.stereotype.Repository;

/**
 * 待办存储库
 *
 * @author clboy
 * @date 2024/05/10 15:28:57
 */
@Repository
public interface TodoRepository extends BaseRepository<Todo, Long> {

}
