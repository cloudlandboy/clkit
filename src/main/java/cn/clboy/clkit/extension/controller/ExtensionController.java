package cn.clboy.clkit.extension.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.extension.entity.Extension;
import cn.clboy.clkit.extension.service.ExtensionService;
import cn.clboy.clkit.extension.vo.ExtensionTypeVO;
import cn.hutool.core.lang.tree.Tree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 扩展控制器
 *
 * @author clboy
 * @date 2024/05/06 10:22:23
 */
@RestController
@Tag(name = "扩展管理")
@RequestMapping("extension")
@RequiredArgsConstructor
public class ExtensionController {

    private final ExtensionService extensionService;

    /**
     * 保存
     *
     * @param dto DTO
     */
    @PostMapping
    public ApiResult<Extension> save(@Validated @RequestBody Extension dto) {
        return ApiResult.ok(extensionService.save(dto));
    }

    /**
     * 更新
     *
     * @param dto DTO
     */
    @PutMapping
    public ApiResult<Extension> update(@Validated @RequestBody Extension dto) {
        return ApiResult.ok(extensionService.updateById(dto));
    }

    /**
     * 获取通过ID
     *
     * @param id ID
     */
    @GetMapping("{id}")
    public ApiResult<Extension> getById(@PathVariable Long id) {
        return ApiResult.ok(extensionService.getById(id));
    }

    /**
     * 获取所有
     */
    @GetMapping
    public ApiResult<List<Extension>> getAll() {
        return ApiResult.ok(extensionService.getAll());
    }

    /**
     * 按ID删除
     *
     * @param id ID
     */
    @DeleteMapping("{id}")
    public ApiResult<Void> removeById(@PathVariable Long id) {
        extensionService.removeById(id);
        return ApiResult.ok();
    }


    /**
     * 获取类型
     */
    @GetMapping("types")
    @Operation(summary = "获取类型列表")
    public ApiResult<List<ExtensionTypeVO>> getTypes() {
        return ApiResult.ok(extensionService.getTypes());
    }

    /**
     * 获取类型
     */
    @GetMapping("tree")
    @Operation(summary = "获取类型列表")
    public ApiResult<List<Tree<Long>>> getTree(Boolean filterInstalled) {
        return ApiResult.ok(extensionService.getTree(filterInstalled));
    }

    /**
     * 获取类型
     */
    @PostMapping("install/{id}")
    @Operation(summary = "安装")
    public ApiResult<Void> install(@PathVariable("id") Long id) {
        extensionService.install(id);
        return ApiResult.ok();
    }
}
