package cn.clboy.clkit.gen.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.gen.entity.Db;
import cn.clboy.clkit.gen.service.DbService;
import cn.clboy.clkit.gen.vo.TableBasicVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DB控制器
 *
 * @author clboy
 * @date 2024/04/18 16:44:10
 */
@RestController
@RequestMapping("gen/db")
@RequiredArgsConstructor
public class DbController {

    private final DbService dbService;

    /**
     * 保存
     *
     * @param dto DTO
     */
    @PostMapping
    @PreAuthorize("@authChecker.hasPermission('gen_db_manage')")
    public ApiResult<Db> save(@Validated @RequestBody Db dto) {
        return ApiResult.ok(dbService.save(dto));
    }

    /**
     * 更新
     *
     * @param dto DTO
     */
    @PutMapping
    @PreAuthorize("@authChecker.hasPermission('gen_db_manage')")
    public ApiResult<Db> update(@Validated @RequestBody Db dto) {
        return ApiResult.ok(dbService.updateById(dto));
    }

    /**
     * 获取通过ID
     *
     * @param id ID
     */
    @GetMapping("{id}")
    @PreAuthorize("@authChecker.hasPermission('gen_db_view')")
    public ApiResult<Db> getById(@PathVariable Long id) {
        return ApiResult.ok(dbService.getById(id));
    }

    /**
     * 获取所有
     */
    @GetMapping
    @PreAuthorize("@authChecker.hasPermission('gen_db_view')")
    public ApiResult<List<Db>> getAll() {
        return ApiResult.ok(dbService.getAll());
    }

    /**
     * 按ID删除
     *
     * @param id ID
     */
    @DeleteMapping("{id}")
    @PreAuthorize("@authChecker.hasPermission('gen_db_manage')")
    public ApiResult<Void> removeById(@PathVariable Long id) {
        dbService.removeById(id);
        return ApiResult.ok();
    }

    /**
     * 查询表
     *
     * @param id      ID
     * @param keyword 关键字
     */
    @GetMapping("/query_table/{id}")
    @PreAuthorize("@authChecker.hasPermission('gen_db_view')")
    public ApiResult<List<TableBasicVO>> queryTable(@PathVariable Long id, String keyword) {
        return ApiResult.ok(dbService.queryTable(id, keyword));
    }

    /**
     * 获取支持的数据库平台
     */
    @GetMapping("get_supported_db_platform")
    @Operation(summary = "获取支持的数据库平台")
    public ApiResult<List<String>> getSupportedPlatform() {
        return ApiResult.ok(dbService.getSupportedPlatform());
    }

}
