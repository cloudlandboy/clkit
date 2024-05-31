package cn.clboy.clkit.upms.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.upms.service.PermissionService;
import cn.hutool.core.lang.tree.Tree;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 权限管理
 *
 * @author clboy
 * @date 2024/05/27 10:31:12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("upms/permission")
public class PermissionController {

    public final PermissionService permissionService;

    /**
     * 获取分页
     */
    @GetMapping("tree")
    @Operation(tags = "获取权限")
    @PreAuthorize("@authChecker.hasPermission('upms_permission_view')")
    public ApiResult<List<Tree<Long>>> getTree() {
        return ApiResult.ok(permissionService.getTree());
    }

}
