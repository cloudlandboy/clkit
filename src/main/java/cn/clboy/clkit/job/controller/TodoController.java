package cn.clboy.clkit.job.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.job.entity.Todo;
import cn.clboy.clkit.job.query.TodoQuery;
import cn.clboy.clkit.job.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 待做事项控制器
 *
 * @author clboy
 * @date 2024/05/10 09:46:05
 */
@Tag(name = "待办管理")
@RestController
@RequestMapping("job/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /**
     * 获取分页通过查询
     *
     * @param page  分页
     * @param query 查询
     */
    @GetMapping("page")
    @Operation(tags = "分页查询")
    public ApiResult<Page<Todo>> getPageByQuery(Pageable page, TodoQuery query) {
        return ApiResult.ok(todoService.getPageByQuery(page, query));
    }

    /**
     * 新增
     *
     * @param todo 待办
     */
    @PostMapping
    @Operation(tags = "新增")
    public ApiResult<Todo> save(@Validated @RequestBody Todo todo) {
        return ApiResult.ok(todoService.save(todo));
    }

    /**
     * 更新
     *
     * @param todo 待办
     */
    @PutMapping
    @Operation(tags = "更新")
    public ApiResult<Todo> update(@Validated @RequestBody Todo todo) {
        return ApiResult.ok(todoService.updateById(todo));
    }

    /**
     * 更新状态
     *
     * @param id     ID
     * @param isDone 完成
     */
    @Operation(tags = "更新状态")
    @PostMapping("update_status/{id}")
    public ApiResult<String> updateStatus(@PathVariable("id") Long id, Boolean isDone) {
        return ApiResult.ok(todoService.updateStatus(id, isDone));
    }
}
