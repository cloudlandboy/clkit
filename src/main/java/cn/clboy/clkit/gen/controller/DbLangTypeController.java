package cn.clboy.clkit.gen.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.gen.entity.DbLangType;
import cn.clboy.clkit.gen.service.DbLangTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据库类型控制器
 *
 * @author clboy
 * @date 2024/04/18 17:16:28
 */
@RestController
@Tag(name = "语言映射管理")
@RequiredArgsConstructor
@RequestMapping("gen/db_lang_type")
public class DbLangTypeController {

    private final DbLangTypeService dbLangTypeService;

    /**
     * 保存
     *
     * @param dto DTO
     */
    @PostMapping
    @PreAuthorize("@authChecker.hasPermission('gen_db_lang_type_manage')")
    public ApiResult<DbLangType> save(@Validated @RequestBody DbLangType dto) {
        return ApiResult.ok(dbLangTypeService.save(dto));
    }

    /**
     * 更新
     *
     * @param dto DTO
     */
    @PutMapping
    @PreAuthorize("@authChecker.hasPermission('gen_db_lang_type_manage')")
    public ApiResult<DbLangType> update(@Validated @RequestBody DbLangType dto) {
        return ApiResult.ok(dbLangTypeService.updateById(dto));
    }

    /**
     * 获取通过ID
     *
     * @param id ID
     */
    @GetMapping("{id}")
    @PreAuthorize("@authChecker.hasPermission('gen_db_lang_type_view')")
    public ApiResult<DbLangType> getById(@PathVariable Long id) {
        return ApiResult.ok(dbLangTypeService.getById(id));
    }

    /**
     * 获取所有
     */
    @GetMapping
    @PreAuthorize("@authChecker.hasPermission('gen_db_lang_type_view')")
    public ApiResult<List<DbLangType>> getAll() {
        return ApiResult.ok(dbLangTypeService.getAll());
    }

    /**
     * 获取所有语言
     */
    @GetMapping("get_all_lang")
    @PreAuthorize("@authChecker.hasPermission('gen_db_lang_type_view')")
    public ApiResult<List<String>> getAllLang() {
        return ApiResult.ok(dbLangTypeService.getAllLang());
    }

    /**
     * 按ID删除
     *
     * @param id ID
     */
    @DeleteMapping("{id}")
    @PreAuthorize("@authChecker.hasPermission('gen_db_lang_type_manage')")
    public ApiResult<Void> removeById(@PathVariable Long id) {
        dbLangTypeService.removeById(id);
        return ApiResult.ok();
    }

    /**
     * 通过ID解锁
     *
     * @param id ID
     */
    @PostMapping("unlock/{id}")
    @PreAuthorize("@authChecker.hasPermission('gen_db_lang_type_manage')")
    public ApiResult<Void> unlockById(@PathVariable Long id) {
        dbLangTypeService.unlockById(id);
        return ApiResult.ok();
    }

    /**
     * 通过DB平台和语言类型获取
     */
    @GetMapping("find")
    @PreAuthorize("@authChecker.hasPermission('gen_db_lang_type_view')")
    public ApiResult<List<DbLangType>> find(String dbPlatform, String langType) {
        return ApiResult.ok(dbLangTypeService.find(dbPlatform, langType));
    }
}
