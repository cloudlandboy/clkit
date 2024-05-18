package cn.clboy.clkit.job.service;

import cn.clboy.clkit.common.service.CrudService;
import cn.clboy.clkit.job.entity.Todo;
import cn.clboy.clkit.job.query.TodoQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 代办服务
 *
 * @author clboy
 * @date 2024/05/10 15:27:00
 */
public interface TodoService extends CrudService<Todo, Long> {

    /**
     * 获取分页通过查询
     *
     * @param page  分页
     * @param query 查询
     */
    Page<Todo> getPageByQuery(Pageable page, TodoQuery query);

    /**
     * 更新状态
     *
     * @param id     ID
     * @param isDone 完成
     */
    String updateStatus(Long id, Boolean isDone);
}
