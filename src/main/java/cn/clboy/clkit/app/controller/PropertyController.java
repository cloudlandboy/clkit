package cn.clboy.clkit.app.controller;

import cn.clboy.clkit.app.entity.ClkitProperty;
import cn.clboy.clkit.app.service.ClkitPropertyService;
import cn.clboy.clkit.common.web.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 按id更新值
     *
     * @param id    ID
     * @param value 值
     */
    @PutMapping("/{id}")
    @Operation(tags = "修改值")
    @PreAuthorize("@authChecker.hasPermission('app_property_config')")
    public ApiResult<ClkitProperty> updateValueById(@PathVariable Long id, @RequestParam("value") String value) {
        return ApiResult.ok(propertyService.updateValueById(id, value));
    }

}
