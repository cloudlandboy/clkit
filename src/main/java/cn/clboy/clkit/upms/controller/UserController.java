package cn.clboy.clkit.upms.controller;

import cn.clboy.clkit.common.web.ApiResult;
import cn.clboy.clkit.upms.service.ClkitUserService;
import cn.clboy.clkit.upms.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
