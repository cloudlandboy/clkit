package cn.clboy.clkit.extension.controller;

import cn.clboy.clkit.common.constants.enums.ExtensionTypeEnum;
import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.extension.entity.Extension;
import cn.clboy.clkit.extension.service.ExtensionService;
import cn.clboy.clkit.extension.vo.ExtensionTypeVO;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@authChecker.hasPermission('extension_manage')")
    public ApiResult<Extension> save(@Validated @RequestBody Extension dto) {
        return ApiResult.ok(extensionService.save(dto));
    }

    /**
     * 更新
     *
     * @param dto DTO
     */
    @PutMapping
    @PreAuthorize("@authChecker.hasPermission('extension_manage')")
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
    @SneakyThrows
    public ApiResult<List<Extension>> getAll() {
        return ApiResult.ok(extensionService.getAll());
    }

    /**
     * 按ID删除
     *
     * @param id ID
     */
    @DeleteMapping("{id}")
    @PreAuthorize("@authChecker.hasPermission('extension_manage')")
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
     * 获取树
     */
    @GetMapping("tree")
    @Operation(summary = "获取树")
    public ApiResult<List<Tree<Long>>> getTree(Boolean filterInstalled) {
        return ApiResult.ok(extensionService.getTree(filterInstalled));
    }

    /**
     * 安装
     */
    @PostMapping("install/{id}")
    @Operation(summary = "安装")
    @PreAuthorize("@authChecker.hasPermission('extension_manage')")
    public ApiResult<Void> install(@PathVariable("id") Long id) {
        extensionService.install(id);
        return ApiResult.ok();
    }
}
