package cn.clboy.clkit.app.controller;

import cn.clboy.clkit.app.entity.ClkitProperty;
import cn.clboy.clkit.app.service.ClkitPropertyService;
import cn.clboy.clkit.common.web.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 参数控制器
 *
 * @author clboy
 * @date 2024/05/21 09:41:37
 */
@RestController
@Tag(name = "参数管理")
@RequiredArgsConstructor
@RequestMapping("app/property")
public class PropertyController {

    private final ClkitPropertyService propertyService;

    /**
     * 获取全部
     */
    @GetMapping
    @Operation(tags = "获取全部")
    public ApiResult<List<ClkitProperty>> getAll() {
        return ApiResult.ok(propertyService.getAll());
    }
}
