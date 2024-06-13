package cn.clboy.clkit.code.controller;

import cn.clboy.clkit.code.entity.Regexp;
import cn.clboy.clkit.code.service.RegexpService;
import cn.clboy.clkit.common.web.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 正则控制器
 *
 * @author clboy
 * @date 2024/06/03 17:29:00
 */
@RestController
@RequestMapping("code/regexp")
@RequiredArgsConstructor
@Tag(name = "正则表达式管理")
public class RegexpController {

    private final RegexpService regexpService;

    /**
     * 获取所有
     */
    @GetMapping
    @Operation(description = "获取全部")
    public ApiResult<List<Regexp>> getAll() {
        return ApiResult.ok(regexpService.getAll());
    }

    /**
     * 新增
     *
     * @param dto DTO
     */
    @PostMapping
    @Operation(description = "新增")
    @PreAuthorize("@authChecker.hasPermission('code_regexp_manage')")
    public ApiResult<Regexp> save(@Validated @RequestBody Regexp dto) {
        return ApiResult.ok(regexpService.save(dto));
    }

    /**
     * 按ID更新
     *
     * @param dto DTO
     */
    @PutMapping
    @Operation(description = "按ID更新")
    @PreAuthorize("@authChecker.hasPermission('code_regexp_manage')")
    public ApiResult<Regexp> updateById(@Validated @RequestBody Regexp dto) {
        return ApiResult.ok(regexpService.updateById(dto));
    }

    /**
     * 按ID删除
     *
     * @param id ID
     */
    @DeleteMapping("{id}")
    @Operation(description = "按ID删除")
    @PreAuthorize("@authChecker.hasPermission('code_regexp_manage')")
    public ApiResult<Void> removeById(@PathVariable Long id) {
        regexpService.removeById(id);
        return ApiResult.ok();
    }

    /**
     * 下载anyRule的js文件
     */
    @Operation(description = "下载anyRule的js文件")
    @GetMapping(value = "any-rule.js", produces = "text/javascript")
    public String downloadAnyRuleJs() {
        return regexpService.downloadAnyRuleJs();
    }

    /**
     * 导入数据
     *
     * @param dataList 数据列表
     */
    @Operation(description = "导入数据")
    @PostMapping(value = "import")
    @PreAuthorize("@authChecker.hasPermission('code_regexp_manage')")
    public ApiResult<Void> importData(@Validated @RequestBody List<@Valid Regexp> dataList) {
        regexpService.importData(dataList);
        return ApiResult.ok();
    }
}
