package cn.clboy.clkit.upms.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.upms.dto.RoleDTO;
import cn.clboy.clkit.upms.entity.Role;
import cn.clboy.clkit.upms.query.RoleQuery;
import cn.clboy.clkit.upms.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 角色管理
 *
 * @author clboy
 * @date 2024/05/27 10:31:12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("upms/role")
@Tag(name = "角色管理")
public class RoleController {

    public final RoleService roleService;

    /**
     * 获取所有
     */
    @GetMapping
    @Operation(tags = "获取所有")
    @PreAuthorize("@authChecker.hasPermission('upms_role_view')")
    public ApiResult<List<Role>> getAll() {
        return ApiResult.ok(roleService.getAll());
    }

    /**
     * 获取分页
     */
    @GetMapping("page")
    @Operation(tags = "分页查询")
    @PreAuthorize("@authChecker.hasPermission('upms_role_view')")
    public ApiResult<Page<Role>> getPage(Pageable page, RoleQuery query) {
        return ApiResult.ok(roleService.getPage(page, query));
    }

    /**
     * 新增角色
     *
     * @param dto dto
     */
    @PostMapping
    @Operation(tags = "新增")
    @PreAuthorize("@authChecker.hasPermission('upms_role_manage')")
    public ApiResult<Role> save(@Validated @RequestBody RoleDTO dto) {
        return ApiResult.ok(roleService.saveByDto(dto));
    }

    /**
     * 通过id更新
     *
     * @param id  id
     * @param dto dto
     */
    @PutMapping("{id}")
    @Operation(tags = "通过id更新")
    @PreAuthorize("@authChecker.hasPermission('upms_role_manage')")
    public ApiResult<Role> updateById(@PathVariable("id") Long id, @Validated @RequestBody RoleDTO dto) {
        return ApiResult.ok(roleService.updateByIdWithDto(id, dto));
    }

    /**
     * 按ID删除
     *
     * @param id ID
     */
    @DeleteMapping("{id}")
    @Operation(tags = "通过id删除")
    @PreAuthorize("@authChecker.hasPermission('upms_role_manage')")
    public ApiResult<Void> removeById(@PathVariable("id") Long id) {
        roleService.removeById(id);
        return ApiResult.ok();
    }

}
