package cn.clboy.clkit.upms.controller;

import cn.clboy.clkit.common.validation.groups.Create;
import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.upms.dto.UserDTO;
import cn.clboy.clkit.upms.dto.UserPasswordDTO;
import cn.clboy.clkit.upms.entity.ClkitUser;
import cn.clboy.clkit.upms.query.UserQuery;
import cn.clboy.clkit.upms.service.ClkitUserService;
import cn.clboy.clkit.upms.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * 互联网控制器
 *
 * @author clboy
 * @date 2024/05/04 09:33:45
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("upms/user")
public class UserController {

    public final ClkitUserService userService;

    /**
     * 获取登录用户信息
     */
    @GetMapping("info")
    @PreAuthorize("isAuthenticated()")
    @Operation(tags = "获取登录用户信息")
    public ApiResult<UserInfoVO> getUserInfo() {
        return ApiResult.ok(userService.getUserInfo());
    }

    /**
     * 获取分页
     */
    @GetMapping("page")
    @Operation(tags = "分页查询")
    @PreAuthorize("@authChecker.hasPermission('upms_user_view')")
    public ApiResult<Page<ClkitUser>> getPage(Pageable page, UserQuery query) {
        return ApiResult.ok(userService.getPage(page, query));
    }

    /**
     * 新增用户
     *
     * @param dto DTO
     */
    @PostMapping
    @Operation(tags = "新增")
    @PreAuthorize("@authChecker.hasPermission('upms_user_manage')")
    public ApiResult<ClkitUser> save(@Validated({Default.class, Create.class}) @RequestBody UserDTO dto) {
        return ApiResult.ok(userService.saveByDto(dto));
    }

    /**
     * 按ID更新
     *
     * @param id  ID
     * @param dto DTO
     */
    @PutMapping("{id}")
    @Operation(tags = "通过id更新")
    @PreAuthorize("@authChecker.hasPermission('upms_user_manage')")
    public ApiResult<ClkitUser> updateById(@PathVariable("id") Long id, @Validated @RequestBody UserDTO dto) {
        return ApiResult.ok(userService.updateByIdWithDto(id, dto));
    }

    /**
     * 按ID删除
     *
     * @param id ID
     */
    @DeleteMapping("{id}")
    @Operation(tags = "通过id删除")
    @PreAuthorize("@authChecker.hasPermission('upms_user_manage')")
    public ApiResult<Void> removeById(@PathVariable("id") Long id) {
        userService.removeById(id);
        return ApiResult.ok();
    }

    /**
     * 重置用户密码
     *
     * @param id ID
     */
    @Operation(tags = "重置用户密码")
    @PutMapping("reset_user_password/{id}")
    @PreAuthorize("@authChecker.hasPermission('upms_user_reset_password')")
    public ApiResult<String> resetUserPassword(@PathVariable Long id) {
        return ApiResult.ok(userService.resetUserPassword(id));
    }

    /**
     * 更新密码
     */
    @PutMapping("password")
    @Operation(tags = "更新密码")
    @PreAuthorize("isAuthenticated()")
    public ApiResult<Void> updatePassword(@Validated @RequestBody UserPasswordDTO dto) {
        userService.updatePassword(dto);
        return ApiResult.ok();
    }

}
