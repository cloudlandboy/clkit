package cn.clboy.clkit.gen.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.gen.entity.CrudTemplate;
import cn.clboy.clkit.gen.service.CrudTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模板控制器
 *
 * @author clboy
 * @date 2024/04/23 16:11:14
 */
@RestController
@RequestMapping("gen/curd_template")
@RequiredArgsConstructor
public class CrudTemplateController {

    private final CrudTemplateService crudTemplateService;

    /**
     * 保存
     *
     * @param dto DTO
     */
    @PostMapping
    public ApiResult<CrudTemplate> save(@Validated @RequestBody CrudTemplate dto) {
        return ApiResult.ok(crudTemplateService.save(dto));
    }

    /**
     * 更新
     *
     * @param dto DTO
     */
    @PutMapping
    public ApiResult<CrudTemplate> update(@Validated @RequestBody CrudTemplate dto) {
        return ApiResult.ok(crudTemplateService.updateById(dto));
    }

    /**
     * 更新
     *
     * @param id ID
     */
    @GetMapping("{id}")
    public ApiResult<CrudTemplate> getById(@PathVariable Long id) {
        return ApiResult.ok(crudTemplateService.getById(id));
    }

    /**
     * 获取所有
     */
    @GetMapping
    public ApiResult<List<CrudTemplate>> getAll() {
        return ApiResult.ok(crudTemplateService.getAll());
    }

    /**
     * 按ID删除
     *
     * @param id ID
     */
    @DeleteMapping("{id}")
    public ApiResult<Void> removeById(@PathVariable Long id) {
        crudTemplateService.removeById(id);
        return ApiResult.ok();
    }

    /**
     * 通过ID解锁
     *
     * @param id ID
     */
    @PutMapping("unlock/{id}")
    public ApiResult<Void> unlockById(@PathVariable Long id) {
        crudTemplateService.unlockById(id);
        return ApiResult.ok();
    }
}
